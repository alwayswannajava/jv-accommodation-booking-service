package com.spring.booking.accommodationbookingservice.controller;

import com.spring.booking.accommodationbookingservice.domain.User;
import com.spring.booking.accommodationbookingservice.dto.role.RoleUpdateRequestDto;
import com.spring.booking.accommodationbookingservice.dto.user.UserResponse;
import com.spring.booking.accommodationbookingservice.dto.user.UserUpdateRequestDto;
import com.spring.booking.accommodationbookingservice.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User management", description = "Endpoints for user managing")
@CacheConfig(cacheNames = "user")
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    @Tag(name = "get", description = "GET methods of Accommodation APIs")
    @Operation(summary = "Get user's info", description = "Get user's info")
    @PreAuthorize("hasRole('CUSTOMER')")
    public UserResponse getUsersInfo(Authentication authentication) {
        return userService.getUserInfo(getUserById(authentication));
    }

    @PutMapping("/{id}/role")
    @Tag(name = "put", description = "PUT methods of Accommodation APIs")
    @Operation(summary = "Update user's role", description = "Update user's role")
    @PreAuthorize("hasRole('ADMIN')")
    @CachePut(key = "#id")
    public UserResponse updateRole(@RequestBody @Valid RoleUpdateRequestDto updateRequestDto,
                                   @PathVariable Long id) {
        return userService.updateRole(updateRequestDto, id);
    }

    @PutMapping("/me")
    @Tag(name = "put", description = "PUT methods of Accommodation APIs")
    @Operation(summary = "Update user's info", description = "Update user's info")
    @PreAuthorize("hasRole('CUSTOMER')")
    public UserResponse updateUserInfo(@Valid
                                           @RequestBody UserUpdateRequestDto requestDto,
                                       Authentication authentication) {
        return userService.updateUserInfo(requestDto, getUserById(authentication));
    }

    private Long getUserById(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return user.getId();
    }
}
