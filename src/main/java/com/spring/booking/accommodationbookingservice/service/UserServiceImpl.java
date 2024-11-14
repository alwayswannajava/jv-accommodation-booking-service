package com.spring.booking.accommodationbookingservice.service;

import com.spring.booking.accommodationbookingservice.domain.Role;
import com.spring.booking.accommodationbookingservice.domain.User;
import com.spring.booking.accommodationbookingservice.domain.enums.RoleName;
import com.spring.booking.accommodationbookingservice.dto.role.RoleUpdateRequestDto;
import com.spring.booking.accommodationbookingservice.dto.user.UserRegistrationRequestDto;
import com.spring.booking.accommodationbookingservice.dto.user.UserResponse;
import com.spring.booking.accommodationbookingservice.dto.user.UserUpdateRequestDto;
import com.spring.booking.accommodationbookingservice.exception.EntityNotFoundException;
import com.spring.booking.accommodationbookingservice.exception.RegistrationException;
import com.spring.booking.accommodationbookingservice.mapper.UserMapper;
import com.spring.booking.accommodationbookingservice.repository.RoleRepository;
import com.spring.booking.accommodationbookingservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponse register(UserRegistrationRequestDto userRegistrationRequestDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(userRegistrationRequestDto.email())) {
            throw new RegistrationException("Can't register user, duplicate email");
        }
        User user = userMapper.toUser(userRegistrationRequestDto);
        Role userRole = roleRepository.findByRole(RoleName.ROLE_CUSTOMER);
        user.setRoles(Set.of(userRole));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse updateRole(RoleUpdateRequestDto roleRequestDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("User with id "
                        + userId
                        + " not found")
        );
        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse getUserInfo(Long userId) {
        return userMapper.toUserResponse(userRepository.findById(userId).get());
    }

    @Override
    public UserResponse updateUserInfo(UserUpdateRequestDto updateRequestDto, Long userId) {
        User user = userRepository.findById(userId).get();
        User updatedUser = userMapper.toUser(user, updateRequestDto);
        userRepository.save(updatedUser);
        return userMapper.toUserResponse(updatedUser);
    }
}
