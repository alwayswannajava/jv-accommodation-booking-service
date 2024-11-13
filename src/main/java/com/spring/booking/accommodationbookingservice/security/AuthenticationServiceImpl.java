package com.spring.booking.accommodationbookingservice.security;

import com.spring.booking.accommodationbookingservice.dto.user.UserLoginRequestDto;
import com.spring.booking.accommodationbookingservice.dto.user.UserLoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public UserLoginResponse authenticate(UserLoginRequestDto userLoginRequestDto) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginRequestDto.email(),
                        userLoginRequestDto.password())
        );
        String token = jwtUtil.generateToken(authentication.getName());
        return new UserLoginResponse(token);
    }
}
