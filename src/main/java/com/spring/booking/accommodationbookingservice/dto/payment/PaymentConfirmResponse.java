package com.spring.booking.accommodationbookingservice.dto.payment;

import com.spring.booking.accommodationbookingservice.domain.enums.Status;
import java.math.BigDecimal;

public record PaymentConfirmResponse(
        String message,
        Long bookingId,
        Status status,
        BigDecimal amount
) {
}
