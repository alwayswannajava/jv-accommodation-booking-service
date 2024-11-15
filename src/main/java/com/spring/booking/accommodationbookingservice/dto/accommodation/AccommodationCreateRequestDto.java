package com.spring.booking.accommodationbookingservice.dto.accommodation;

import com.spring.booking.accommodationbookingservice.domain.enums.Type;
import com.spring.booking.accommodationbookingservice.dto.amenity.AmenityDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

public record AccommodationCreateRequestDto(
        @NotNull
        Type type,
        @NotBlank
        @Size(min = 4, max = 30, message = "must be between 4 and 30 characters")
        String country,
        @NotBlank
        @Size(min = 2, max = 30, message = "must be between 4 and 30 characters")
        String city,
        @NotBlank
        @Size(min = 4, max = 255, message = "must be between 4 and 255 characters")
        String street,
        @NotBlank
        @Size(min = 4, max = 30, message = "must be between 4 and 30 characters")
        String postalCode,
        @NotBlank
        @Size(min = 4, max = 255, message = "must be between 4 and 255 characters")
        String size,
        @NotEmpty
        @Valid
        List<AmenityDto> amenities,
        @NotNull
        @Positive
        BigDecimal dailyRate,
        @NotNull
        @PositiveOrZero
        Integer availability
) {
}
