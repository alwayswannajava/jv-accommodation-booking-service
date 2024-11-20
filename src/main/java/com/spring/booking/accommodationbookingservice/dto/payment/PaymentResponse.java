package com.spring.booking.accommodationbookingservice.dto.payment;

import com.spring.booking.accommodationbookingservice.domain.enums.Status;
import java.math.BigDecimal;
import java.net.URL;

public record PaymentResponse(
        Status status,
        Long bookingId,
        URL sessionUrl,
        Long sessionId,
        BigDecimal amountToPay
) {
}
