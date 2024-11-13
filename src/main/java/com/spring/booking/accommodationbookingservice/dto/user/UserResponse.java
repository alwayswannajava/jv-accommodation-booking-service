package com.spring.booking.accommodationbookingservice.dto.user;

public record UserResponse(
        Long id,
        String email,
        String firstName,
        String lastName
) {
}
