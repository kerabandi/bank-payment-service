package com.bank.payment.service;

import java.math.BigDecimal;
import java.util.UUID;


import static org.junit.jupiter.api.Assertions.assertEquals;
import com.bank.payment.dto.CreatePaymentRequest;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

@QuarkusTest
class PaymentServiceTest {

    @Inject
    PaymentService paymentService;

    @Inject
    EntityManager entityManager;

    @Test
    void shouldReturnSamePaymentForDuplicateRequest() {

        String key = "TEST-" + UUID.randomUUID();

        CreatePaymentRequest request = new CreatePaymentRequest(
                1L,
                2L,
                new BigDecimal("150.00")
        );

        UUID first = paymentService.createPayment(request, key);
        UUID second = paymentService.createPayment(request, key);

        assertEquals(first, second);

        assertEquals(1L, countPayments(key));
        assertEquals(1L, countOutboxEvents(first));
    }

    @Transactional
    long countPayments(String key) {
        return entityManager.createQuery(
                        """
                                SELECT COUNT(p)
                                FROM Payment p
                                WHERE p.idempotencyKey = :key
                                """,
                        Long.class)
                .setParameter("key", key)
                .getSingleResult();
    }

    @Transactional
    long countOutboxEvents(UUID paymentId) {
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

    @Test
    void shouldPreventConcurrentDuplicatePayments() throws Exception {

        String key = "CONCURRENT-" + UUID.randomUUID();

        CreatePaymentRequest request = new CreatePaymentRequest(
                1L,
                2L,
                new BigDecimal("250.00")
        );

        try (var executor =
                     java.util.concurrent.Executors.newVirtualThreadPerTaskExecutor()) {

            var start = new java.util.concurrent.CountDownLatch(1);

            var task = (java.util.concurrent.Callable<UUID>) () -> {
                start.await();
                return paymentService.createPayment(request, key);
            };

            var firstFuture = executor.submit(task);
            var secondFuture = executor.submit(task);

            start.countDown();

            UUID first = firstFuture.get();
            UUID second = secondFuture.get();

            assertEquals(first, second);
            assertEquals(1L, countPayments(key));
            assertEquals(1L, countOutboxEvents(first));
        }
    }
}