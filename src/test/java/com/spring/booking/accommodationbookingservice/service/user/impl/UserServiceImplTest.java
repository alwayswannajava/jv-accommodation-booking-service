package com.spring.booking.accommodationbookingservice.service.user.impl;

import static com.spring.booking.accommodationbookingservice.util.Constants.CORRECT_USER_EMAIL;
import static com.spring.booking.accommodationbookingservice.util.Constants.CORRECT_USER_ID;
import static com.spring.booking.accommodationbookingservice.util.Constants.ENCODED_USER_PASSWORD;
import static com.spring.booking.accommodationbookingservice.util.Constants.EXPECTED_DUPLICATE_EMAIL_USER_REGISTER_MESSAGE;
import static com.spring.booking.accommodationbookingservice.util.Constants.UPDATED_ENCODED_USER_PASSWORD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.spring.booking.accommodationbookingservice.domain.Role;
import com.spring.booking.accommodationbookingservice.domain.User;
import com.spring.booking.accommodationbookingservice.domain.enums.RoleName;
import com.spring.booking.accommodationbookingservice.dto.role.RoleUpdateRequestDto;
import com.spring.booking.accommodationbookingservice.dto.user.UserRegistrationRequestDto;
import com.spring.booking.accommodationbookingservice.dto.user.UserResponse;
import com.spring.booking.accommodationbookingservice.dto.user.UserUpdateRequestDto;
import com.spring.booking.accommodationbookingservice.exception.RegistrationException;
import com.spring.booking.accommodationbookingservice.mapper.UserMapper;
import com.spring.booking.accommodationbookingservice.repository.RoleRepository;
import com.spring.booking.accommodationbookingservice.repository.UserRepository;
import com.spring.booking.accommodationbookingservice.util.TestUtil;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRegistrationRequestDto registrationRequestDto;
    private RoleUpdateRequestDto roleUpdateRequestDto;
    private UserUpdateRequestDto userUpdateRequestDto;
    private UserResponse userResponse;
    private UserResponse updatedUserResponse;
    private User updatedUser;
    private User user;

    @BeforeEach
    void setUp() {
        registrationRequestDto = TestUtil.createRegisterUser();

        roleUpdateRequestDto = TestUtil.createRoleUpdateDto();

        userUpdateRequestDto = TestUtil.createUserUpdateDto();

        userResponse = TestUtil.createUserResponse();

        updatedUserResponse = TestUtil.createUpdatedUserResponse();

        updatedUser = TestUtil.createUpdatedUser();

        user = TestUtil.createUser();
    }


    @Test
    @DisplayName("Test register() method")
    void register_ValidUserRegistrationRequestDto_ReturnUserResponse()
            throws RegistrationException {
        when(userRepository.existsByEmail(CORRECT_USER_EMAIL))
                .thenReturn(false);
        when(userMapper.toUser(registrationRequestDto)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(userResponse);
        Role role = TestUtil.createCustomerRole();
        when(roleRepository.findByRole(RoleName.ROLE_CUSTOMER))
                .thenReturn(role);
        when(passwordEncoder.encode(user.getPassword()))
                .thenReturn(ENCODED_USER_PASSWORD);
        when(userRepository.save(user)).thenReturn(user);

        UserResponse expected = userResponse;
        UserResponse actual = userService.register(registrationRequestDto);
        assertEquals(expected, actual);

        verify(userRepository).existsByEmail(CORRECT_USER_EMAIL);
        verify(userMapper).toUser(registrationRequestDto);
        verify(roleRepository).findByRole(RoleName.ROLE_CUSTOMER);
        verify(userRepository).save(user);
        verifyNoMoreInteractions(userRepository, roleRepository, userMapper, passwordEncoder);

    }

    @Test
    @DisplayName("Test register() method by duplicate email")
    void register_DuplicateEmail_ThrowRegistrationException() {
        when(userRepository.existsByEmail(registrationRequestDto.email()))
                .thenReturn(true);
        RegistrationException registrationException = assertThrows(
                RegistrationException.class,
                () -> userService.register(registrationRequestDto));

        String actualMessage = registrationException.getMessage();

        assertEquals(EXPECTED_DUPLICATE_EMAIL_USER_REGISTER_MESSAGE,
                actualMessage);
        verify(userRepository).existsByEmail(registrationRequestDto.email());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("Test updateRole() method")
    void updateRole_ValidRoleUpdateRequestDto_ReturnUserResponse() {
        when(userRepository.findById(CORRECT_USER_ID))
                .thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(updatedUserResponse);
        when(roleRepository.findByRole(RoleName.ROLE_CUSTOMER))
                .thenReturn(TestUtil.createCustomerRole());

        UserResponse expected = updatedUserResponse;
        UserResponse actual = userService.updateRole(roleUpdateRequestDto, CORRECT_USER_ID);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test updateUserInfo() method")
    void updateUserInfo_ValidUserUpdateRequestDto_ReturnUserResponse() {
        when(userRepository.findById(CORRECT_USER_ID))
                .thenReturn(Optional.of(user));
        when(userMapper.toUser(user, userUpdateRequestDto)).thenReturn(updatedUser);
        when(passwordEncoder.encode(updatedUser.getPassword()))
                .thenReturn(UPDATED_ENCODED_USER_PASSWORD);
        when(userMapper.toResponse(updatedUser)).thenReturn(updatedUserResponse);

        UserResponse expected = updatedUserResponse;
        UserResponse actual = userService.updateUserInfo(userUpdateRequestDto, CORRECT_USER_ID);

        assertEquals(expected, actual);

        verify(userRepository).findById(CORRECT_USER_ID);
        verify(userMapper).toUser(user, userUpdateRequestDto);
        verify(passwordEncoder).encode(updatedUser.getPassword());
        verify(userMapper).toResponse(updatedUser);
        verifyNoMoreInteractions(userRepository, roleRepository, userMapper, passwordEncoder);
    }
}
