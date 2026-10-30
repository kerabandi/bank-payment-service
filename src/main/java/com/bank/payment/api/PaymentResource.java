package com.bank.payment.api;

import com.bank.payment.dto.PaymentRequest;
import com.bank.payment.dto.PaymentResponse;
import com.bank.payment.service.PaymentService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/payments")
@Produces(MediaType.APPLICATION_JSON)
public class PaymentResource {

    private final PaymentService paymentService;

    @Inject
    public PaymentResource(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @POST
    public Response createPayment(PaymentRequest request) {

        PaymentResponse result = paymentService.createPayment(request);

        return Response.status(Response.Status.CREATED)
                .entity(result)
                .build();
    }
}
