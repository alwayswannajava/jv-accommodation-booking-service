package com.spring.booking.accommodationbookingservice.dto.user;

import java.io.Serializable;

public record UserResponse(
        Long id,
        String email,
        String firstName,
        String lastName
) implements Serializable {
}
