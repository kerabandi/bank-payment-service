package com.bank.payment.dto;

public record PaymentEvent(String eventId,
                           String paymentId,
                           String accountId,
                           Double amount,
                           String currency) {
}
