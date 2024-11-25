package com.spring.booking.accommodationbookingservice.dto.amenity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

public record AmenityDto(
        @NotBlank
        @Size(min = 4, max = 50, message = "must be between 4 and 50")
        String title,
        @Size(min = 4, max = 50, message = "must be between 4 and 50")
        String description
) implements Serializable {
}
