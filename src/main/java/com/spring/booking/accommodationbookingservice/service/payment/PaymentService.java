package com.spring.booking.accommodationbookingservice.service.payment;

import com.spring.booking.accommodationbookingservice.dto.payment.PaymentCancelResponse;
import com.spring.booking.accommodationbookingservice.dto.payment.PaymentConfirmResponse;
import com.spring.booking.accommodationbookingservice.dto.payment.PaymentCreateRequestDto;
import com.spring.booking.accommodationbookingservice.dto.payment.PaymentResponse;
import com.stripe.exception.StripeException;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface PaymentService {
    List<PaymentResponse> findAll(Authentication authentication);

    List<PaymentResponse> findAllByUserId(Long userId);

    PaymentResponse create(PaymentCreateRequestDto createRequestDto) throws StripeException;

    PaymentConfirmResponse confirm() throws StripeException;

    PaymentCancelResponse cancel() throws StripeException;

}
