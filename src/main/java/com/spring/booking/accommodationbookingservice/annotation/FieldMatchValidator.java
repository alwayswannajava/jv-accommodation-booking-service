package com.spring.booking.accommodationbookingservice.annotation;

import com.spring.booking.accommodationbookingservice.dto.user.UserRegistrationRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;

public class FieldMatchValidator implements
        ConstraintValidator<FieldMatch, UserRegistrationRequestDto> {

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
    }

    @Override
    public boolean isValid(UserRegistrationRequestDto userRegistrationRequestDto,
                           ConstraintValidatorContext constraintValidatorContext) {
        if (userRegistrationRequestDto == null) {
            return false;
        }
        if (!Objects.equals(userRegistrationRequestDto.password(),
                userRegistrationRequestDto.repeatPassword())) {
            return false;
        }
        return userRegistrationRequestDto.password()
                .equals(userRegistrationRequestDto.repeatPassword());
    }
}
