package com.bank.payment.service;

import java.util.UUID;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
class PaymentOutboxRollbackTest {

    @Inject
    PaymentRollbackTestService rollbackService;

    @Test
    void shouldRollbackPaymentWhenOutboxFails() {

        UUID paymentId = UUID.randomUUID();

        assertThrows(
                Exception.class,
                () -> rollbackService.persistInvalidPaymentAndOutbox(paymentId)
        );

        assertEquals(0L, rollbackService.countPayments(paymentId));
        assertEquals(0L, rollbackService.countOutboxEvents(paymentId));
    }
}