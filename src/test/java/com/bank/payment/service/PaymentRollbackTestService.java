package com.bank.payment.service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;


import com.bank.payment.entity.Account;
import com.bank.payment.entity.OutboxEvent;
import com.bank.payment.entity.Payment;
import com.bank.payment.entity.PaymentStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class PaymentRollbackTestService {

    @Inject
    EntityManager entityManager;

    @Transactional
    public void persistInvalidPaymentAndOutbox(UUID paymentId) {

        Account source = entityManager.find(Account.class, 1L);
        Account target = entityManager.find(Account.class, 2L);

        Payment payment = new Payment();
        payment.setPaymentId(paymentId);
        payment.setIdempotencyKey("ROLLBACK-" + paymentId);
        payment.setSourceAccount(source);
        payment.setTargetAccount(target);
        payment.setAmount(new BigDecimal("100.00"));
        payment.setCurrency("EUR");
        payment.setStatus(PaymentStatus.PENDING);

        entityManager.persist(payment);
        entityManager.flush();

        OutboxEvent invalidEvent = new OutboxEvent(
                UUID.randomUUID(),
                "Payment",
                paymentId,
                null,
                Map.of("paymentId", paymentId.toString())
        );

        entityManager.persist(invalidEvent);
        entityManager.flush();
    }

    @Transactional
    public long countPayments(UUID paymentId) {

        return entityManager.createQuery(
                        """
                                SELECT COUNT(p)
                                FROM Payment p
                                WHERE p.paymentId = :paymentId
                                """,
                        Long.class)
                .setParameter("paymentId", paymentId)
                .getSingleResult();
    }

    @Transactional
    public long countOutboxEvents(UUID paymentId) {

        return entityManager.createQuery(
                        """
                                SELECT COUNT(o)
                                FROM OutboxEvent o
                                WHERE o.aggregateId = :paymentId
                                """,
                        Long.class)
                .setParameter("paymentId", paymentId)
                .getSingleResult();
    }
}