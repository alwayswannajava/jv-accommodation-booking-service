package com.spring.booking.accommodationbookingservice.mapper;

import com.spring.booking.accommodationbookingservice.config.MapperConfig;
import com.spring.booking.accommodationbookingservice.domain.Payment;
import com.spring.booking.accommodationbookingservice.dto.payment.PaymentResponse;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface PaymentMapper {
    PaymentResponse toPaymentResponse(Payment payment);
}
