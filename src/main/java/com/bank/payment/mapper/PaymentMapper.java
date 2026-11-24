package com.bank.payment.mapper;

import com.bank.payment.dto.CreatePaymentRequest;
import com.bank.payment.dto.PaymentResponse;
import com.bank.payment.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "cdi")
public interface PaymentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "paymentId", ignore = true)
    @Mapping(target = "idempotencyKey", ignore = true)
    @Mapping(target = "sourceAccount", ignore = true)
    @Mapping(target = "targetAccount", ignore = true)
    @Mapping(target = "currency", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "completedAt", ignore = true)
    Payment toEntity(CreatePaymentRequest request);

    @Mapping(target = "sourceAccountId",
            source = "sourceAccount.id")
    @Mapping(target = "targetAccountId",
            source = "targetAccount.id")
    PaymentResponse toResponse(Payment payment);

}
