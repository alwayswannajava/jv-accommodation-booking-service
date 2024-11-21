package com.spring.booking.accommodationbookingservice.mapper;

import com.spring.booking.accommodationbookingservice.config.MapperConfig;
import com.spring.booking.accommodationbookingservice.domain.Payment;
import com.spring.booking.accommodationbookingservice.dto.payment.PaymentResponse;
import com.stripe.model.checkout.Session;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface PaymentMapper {
    PaymentResponse toPaymentResponse(Payment payment);

    @Mapping(target = "status", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sessionId", source = "id")
    @Mapping(target = "sessionUrl", source = "url")
    @Mapping(source = "amountTotal", target = "amountToPay")
    Payment toModel(Session session);
}
