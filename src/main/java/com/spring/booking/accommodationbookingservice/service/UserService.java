package com.spring.booking.accommodationbookingservice.service;

import com.spring.booking.accommodationbookingservice.dto.role.RoleUpdateRequestDto;
import com.spring.booking.accommodationbookingservice.dto.user.UserRegistrationRequestDto;
import com.spring.booking.accommodationbookingservice.dto.user.UserResponse;
import com.spring.booking.accommodationbookingservice.dto.user.UserUpdateRequestDto;
import com.spring.booking.accommodationbookingservice.exception.RegistrationException;

public interface UserService {
    UserResponse register(UserRegistrationRequestDto userRegistrationRequestDto)
            throws RegistrationException;

    UserResponse updateRole(RoleUpdateRequestDto roleRequestDto, Long userId);

    UserResponse getUserInfo(Long userId);

    UserResponse updateUserInfo(UserUpdateRequestDto updateRequestDto, Long userId);
}
