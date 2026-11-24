package com.bank.payment.service;

import java.util.Map;
import java.util.UUID;


import com.bank.payment.dto.CreatePaymentRequest;
import com.bank.payment.dto.LockedAccounts;
import com.bank.payment.entity.Account;
import com.bank.payment.entity.OutboxEvent;
import com.bank.payment.entity.Payment;
import com.bank.payment.entity.PaymentStatus;
import com.bank.payment.exception.PaymentConflictException;
import com.bank.payment.mapper.PaymentMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class PaymentTransactionService {

    private final EntityManager em;
    private final PaymentMapper mapper;

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public UUID createPayment(CreatePaymentRequest request, String idempotencyKey) {

        Payment existing = findByIdempotencyKey(idempotencyKey);

        if (existing != null) {
            validateSameRequest(existing, request);
            return existing.getPaymentId();
        }

        LockedAccounts accounts = lockAccounts(request.sourceAccountId(), request.targetAccountId());

        Account source = accounts.source();
        Account target = accounts.target();

        creditAndDebitTransactions(request);

        Payment payment = mapper.toEntity(request);

        payment.setPaymentId(UUID.randomUUID());
        payment.setIdempotencyKey(idempotencyKey);
        payment.setSourceAccount(source);
        payment.setTargetAccount(target);
        payment.setCurrency("EUR");
        payment.setStatus(PaymentStatus.PENDING);

        em.persist(payment);

        OutboxEvent event = new OutboxEvent(
                UUID.randomUUID(),
                "Payment",
                payment.getPaymentId(),
                "PaymentCreated",
                Map.of(
                        "paymentId", payment.getPaymentId().toString(),
                        "sourceAccountId", source.getId(),
                        "targetAccountId", target.getId(),
                        "amount", payment.getAmount(),
                        "currency", "EUR",
                        "status", PaymentStatus.PENDING.name()
                )
        );

        em.persist(event);

        return payment.getPaymentId();
    }

    private void creditAndDebitTransactions(CreatePaymentRequest request) {
        int debitedRows = em.createQuery("""
                        UPDATE Account a
                        SET a.balance = a.balance - :amount
                        WHERE a.id = :sourceId
                          AND a.balance >= :amount
                        """)
                .setParameter("amount", request.amount())
                .setParameter("sourceId", request.sourceAccountId())
                .executeUpdate();

        if (debitedRows != 1) {
            throw new IllegalStateException("Insufficient funds");
        }

        int accreditedRows = em.createQuery("""
                        UPDATE Account a
                        SET a.balance = a.balance + :amount
                        WHERE a.id = :targetId
                        """)
                .setParameter("amount", request.amount())
                .setParameter("targetId", request.targetAccountId())
                .executeUpdate();

        if (accreditedRows != 1) {
            throw new IllegalStateException("account not found");
        }
    }

    private LockedAccounts lockAccounts(Long sourceId, Long targetId) {
        long firstId = Math.min(sourceId, targetId);
        long secondId = Math.max(sourceId, targetId);

        Account first = em.find(Account.class, firstId, LockModeType.PESSIMISTIC_WRITE);
        Account second = em.find(Account.class, secondId, LockModeType.PESSIMISTIC_WRITE);

        if (first == null || second == null) {
            throw new IllegalArgumentException("Source or target account not found");
        }

        if (sourceId == firstId) {
            return  new LockedAccounts(first, second);
        } else {
            return   new LockedAccounts(second, first);
        }
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public Payment findByIdempotencyKey(String key) {

        return em.createQuery(
                        """
                                SELECT p FROM Payment p
                                JOIN FETCH p.sourceAccount
                                JOIN FETCH p.targetAccount
                                WHERE p.idempotencyKey = :key
                                """,
                        Payment.class)
                .setParameter("key", key)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    public void validateSameRequest(Payment existing, CreatePaymentRequest request) {

        boolean sameRequest =
                existing.getSourceAccount().getId()
                        .equals(request.sourceAccountId())
                && existing.getTargetAccount().getId()
                        .equals(request.targetAccountId())
                && existing.getAmount()
                           .compareTo(request.amount()) == 0
                && "EUR".equals(existing.getCurrency());

        if (!sameRequest) {
            throw new PaymentConflictException(
                    "Idempotency key already used with different payment data");
        }
    }
}