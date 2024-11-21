package com.spring.booking.accommodationbookingservice.dto.payment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PaymentCreateRequestDto(
        @Positive
        @NotNull
        Long bookingId
) {
}
