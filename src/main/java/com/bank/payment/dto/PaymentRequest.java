package com.bank.payment.dto;

import java.math.BigDecimal;

public record PaymentRequest(
        String debtorAccount,
        String creditorAccount,
        BigDecimal amount,
        String currency
) {}
