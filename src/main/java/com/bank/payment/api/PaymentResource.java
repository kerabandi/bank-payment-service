package com.bank.payment.api;

import java.util.UUID;


import com.bank.payment.dto.CreatePaymentRequest;
import com.bank.payment.service.PaymentService;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Path("/api/payments")
@Produces(MediaType.APPLICATION_JSON)
public class PaymentResource {

    private final PaymentService paymentService;

    @POST
    public Response createPayment(CreatePaymentRequest request, @HeaderParam("Idempotency-Key") String idempotencyKey) {


        UUID paymentUuid = paymentService.createPayment(request, idempotencyKey);

        return Response.status(Response.Status.CREATED)
                .entity(paymentUuid)
                .build();
    }
}
