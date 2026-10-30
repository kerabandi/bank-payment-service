package com.bank.payment.service;

import java.util.UUID;


import com.bank.payment.dto.PaymentRequest;
import com.bank.payment.dto.PaymentResponse;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PaymentService {

    public PaymentResponse createPayment(PaymentRequest request) {

        UUID paymentId = UUID.randomUUID();

        return new PaymentResponse(
                paymentId,
                "RECEIVED",
                request.amount(),
                request.currency()
        );
    }
}
