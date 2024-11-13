package com.spring.booking.accommodationbookingservice.service;

import com.spring.booking.accommodationbookingservice.dto.user.UserRegistrationRequestDto;
import com.spring.booking.accommodationbookingservice.dto.user.UserResponse;
import com.spring.booking.accommodationbookingservice.exception.RegistrationException;

public interface UserService {
    UserResponse register(UserRegistrationRequestDto userRegistrationRequestDto)
            throws RegistrationException;
}
