package com.spring.booking.accommodationbookingservice.dto.user;

import com.spring.booking.accommodationbookingservice.annotation.FieldMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@FieldMatch(firstField = "password",
        secondField = "repeatPassword",
        message = "Passwords must be match")
public record UserUpdateRequestDto(
        @NotBlank
        @Email
        String email,
        @NotBlank
        @Size(min = 8, max = 25)
        String password,
        @NotBlank
        @Size(min = 8, max = 25)
        String repeatPassword,
        @NotBlank
        String firstName,
        @NotBlank
        String lastName
) {
}
