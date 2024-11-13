package com.spring.booking.accommodationbookingservice.dto.user;

import jakarta.validation.constraints.NotBlank;

public record UserLoginRequestDto(
        @NotBlank String email,
        @NotBlank String password
) {}
