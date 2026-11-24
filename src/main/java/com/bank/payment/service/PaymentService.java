package com.bank.payment.service;

import java.math.BigDecimal;
import java.util.UUID;


import com.bank.payment.dto.CreatePaymentRequest;
import com.bank.payment.entity.Payment;
import com.bank.payment.exception.PostgresConstraintUtil;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentTransactionService transactionService;

    public UUID createPayment(CreatePaymentRequest request, String idempotencyKey) {

        validateRequest(request, idempotencyKey);

        try {
            return transactionService.createPayment(request, idempotencyKey);

        } catch (RuntimeException exception) {
            if (!PostgresConstraintUtil.isIdempotencyConflict(exception)) {
                throw exception;
            }

            Payment existing = transactionService.findByIdempotencyKey(idempotencyKey);

            if (existing == null) {
                throw exception;
            }
            transactionService.validateSameRequest(existing, request);

            return existing.getPaymentId();
        }
    }

    private void validateRequest(CreatePaymentRequest request, String key) {

        if (request == null
            || request.sourceAccountId() == null
            || request.targetAccountId() == null
            || request.amount() == null) {

            throw new IllegalArgumentException("Missing payment data");
        }

        if (key == null || key.isBlank() || key.length() > 100) {
            throw new IllegalArgumentException(
                    "Invalid Idempotency-Key");
        }

        if (request.sourceAccountId().equals(request.targetAccountId())) {
            throw new IllegalArgumentException("Source and target accounts must differ");
        }

        BigDecimal amount = request.amount();

        if (amount.signum() <= 0
            || amount.scale() > 2
            || amount.precision() - amount.scale() > 13) {

            throw new IllegalArgumentException(
                    "Invalid payment amount");
        }
    }
}