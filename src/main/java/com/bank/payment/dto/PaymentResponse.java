package com.bank.payment.dto;

import java.math.BigDecimal;
import java.util.UUID;


import com.bank.payment.entity.PaymentStatus;

public record PaymentResponse(
        UUID paymentId,
        Long sourceAccountId,
        Long targetAccountId,
        BigDecimal amount,
        String currency,
        PaymentStatus status
) {
}