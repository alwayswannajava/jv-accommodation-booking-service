package com.spring.booking.accommodationbookingservice.mapper;

import com.spring.booking.accommodationbookingservice.config.MapperConfig;
import com.spring.booking.accommodationbookingservice.domain.User;
import com.spring.booking.accommodationbookingservice.dto.user.UserRegistrationRequestDto;
import com.spring.booking.accommodationbookingservice.dto.user.UserResponse;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponse toUserResponse(User user);

    User toUser(UserRegistrationRequestDto userRegistrationRequestDto);
}
