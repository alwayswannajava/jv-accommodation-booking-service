package com.spring.booking.accommodationbookingservice.dto.booking;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.spring.booking.accommodationbookingservice.domain.enums.Status;
import java.io.Serializable;
import java.time.LocalDate;

public record BookingResponse(
        @JsonFormat(pattern = "dd-MM-yyyy")
        LocalDate checkInDate,
        @JsonFormat(pattern = "dd-MM-yyyy")
        LocalDate checkOutDate,
        Long accommodationId,
        Long userId,
        Status status
) implements Serializable {
}
