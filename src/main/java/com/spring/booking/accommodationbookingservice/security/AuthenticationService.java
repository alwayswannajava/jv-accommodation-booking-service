package com.spring.booking.accommodationbookingservice.security;

import com.spring.booking.accommodationbookingservice.dto.user.UserLoginRequestDto;
import com.spring.booking.accommodationbookingservice.dto.user.UserLoginResponse;

public interface AuthenticationService {
    UserLoginResponse authenticate(UserLoginRequestDto userLoginRequestDto);
}
