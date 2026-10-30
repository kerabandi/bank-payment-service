package com.bank.payment.messaging;

import com.bank.payment.dto.PaymentEvent;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class PaymentConsumer {

    @Incoming("payments-in")
    public void consume(PaymentEvent event) {
        System.out.println("Pagamento ricevuto: " + event);
    }

   /* @Incoming("payments-in")
    public void consume(Message<PaymentEvent> message) {
        PaymentEvent event = message.getPayload();
        System.out.println("Pagamento ricevuto: " + event);
    }*/
}
