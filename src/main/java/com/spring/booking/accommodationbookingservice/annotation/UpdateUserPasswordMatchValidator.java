package com.spring.booking.accommodationbookingservice.annotation;

import com.spring.booking.accommodationbookingservice.dto.user.UserUpdateRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;

public class UpdateUserPasswordMatchValidator implements
        ConstraintValidator<PasswordMatch, UserUpdateRequestDto> {

    @Override
    public boolean isValid(UserUpdateRequestDto updateRequestDto,
                           ConstraintValidatorContext constraintValidatorContext) {
        if (updateRequestDto == null) {
            return false;
        }
        if (!Objects.equals(updateRequestDto.password(),
                updateRequestDto.repeatPassword())) {
            return false;
        }
        return updateRequestDto.password()
                .equals(updateRequestDto.repeatPassword());
    }
}
