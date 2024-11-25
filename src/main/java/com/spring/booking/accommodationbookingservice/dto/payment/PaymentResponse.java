package com.spring.booking.accommodationbookingservice.dto.payment;

import com.spring.booking.accommodationbookingservice.domain.enums.Status;

public record PaymentResponse(
        Status status,
        Long bookingId,
        String sessionUrl,
        String sessionId,
        Long amountToPay
) {
}
