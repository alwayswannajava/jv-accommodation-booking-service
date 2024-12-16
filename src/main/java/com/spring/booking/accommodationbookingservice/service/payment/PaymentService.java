package com.spring.booking.accommodationbookingservice.service.payment;

import com.spring.booking.accommodationbookingservice.dto.payment.PaymentCancelResponse;
import com.spring.booking.accommodationbookingservice.dto.payment.PaymentConfirmResponse;
import com.spring.booking.accommodationbookingservice.dto.payment.PaymentCreateRequestDto;
import com.spring.booking.accommodationbookingservice.dto.payment.PaymentResponse;
import com.stripe.exception.StripeException;
import java.util.List;

public interface PaymentService {
    List<PaymentResponse> findAllByUserId(Long userId);

    PaymentResponse create(PaymentCreateRequestDto createRequestDto) throws StripeException;

    PaymentConfirmResponse confirm(String sessionId) throws StripeException;

    PaymentCancelResponse cancel(String sessionId) throws StripeException;

}
