package com.spring.booking.accommodationbookingservice.controller;

import com.spring.booking.accommodationbookingservice.dto.user.UserLoginRequestDto;
import com.spring.booking.accommodationbookingservice.dto.user.UserLoginResponse;
import com.spring.booking.accommodationbookingservice.dto.user.UserRegistrationRequestDto;
import com.spring.booking.accommodationbookingservice.dto.user.UserResponse;
import com.spring.booking.accommodationbookingservice.exception.RegistrationException;
import com.spring.booking.accommodationbookingservice.security.AuthenticationService;
import com.spring.booking.accommodationbookingservice.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth management", description = "Endpoints for managing user's authentication")
public class AuthController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Tag(name = "post", description = "POST methods of Accommodation APIs")
    @Operation(summary = "Registration new user", description = "Registration new user")
    public UserResponse registration(@RequestBody
                                     @Valid
                                         UserRegistrationRequestDto userRegistrationRequestDto)
            throws RegistrationException {
        return userService.register(userRegistrationRequestDto);
    }

    @PostMapping("/login")
    @Tag(name = "post", description = "POST methods of Accommodation APIs")
    @Operation(summary = "Login user", description = "Login user")
    public UserLoginResponse login(@RequestBody @Valid UserLoginRequestDto userLoginRequestDto) {
        return authenticationService.authenticate(userLoginRequestDto);
    }
}
