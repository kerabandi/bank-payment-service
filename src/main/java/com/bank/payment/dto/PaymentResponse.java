package com.bank.payment.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentResponse(
        UUID paymentId,
        String status,
        BigDecimal amount,
        String currency
) {}