package com.bank.payment.dto;

import java.math.BigDecimal;

public record CreatePaymentRequest(Long sourceAccountId,
                                   Long targetAccountId,
                                   BigDecimal amount) {
}
