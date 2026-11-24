package com.bank.payment.exception;

public class PaymentConflictException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public PaymentConflictException(String message) {
        super(message);
    }
}
