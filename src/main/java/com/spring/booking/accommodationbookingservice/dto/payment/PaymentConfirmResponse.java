package com.spring.booking.accommodationbookingservice.dto.payment;

import com.spring.booking.accommodationbookingservice.domain.enums.Status;
import java.math.BigDecimal;
import java.net.URL;

public record PaymentConfirmResponse(
        String message,
        URL paymentUrl,
        String paymentId,
        Status status,
        BigDecimal amount
) {
}
