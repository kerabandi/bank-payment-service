package com.bank.payment.messaging;

import com.bank.payment.dto.PaymentEvent;
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class PaymentDeserializer extends ObjectMapperDeserializer<PaymentEvent> {
    public PaymentDeserializer() {
        super(PaymentEvent.class);
    }
}
