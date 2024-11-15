package com.spring.booking.accommodationbookingservice.mapper;

import com.spring.booking.accommodationbookingservice.config.MapperConfig;
import com.spring.booking.accommodationbookingservice.domain.User;
import com.spring.booking.accommodationbookingservice.dto.role.RoleUpdateRequestDto;
import com.spring.booking.accommodationbookingservice.dto.user.UserRegistrationRequestDto;
import com.spring.booking.accommodationbookingservice.dto.user.UserResponse;
import com.spring.booking.accommodationbookingservice.dto.user.UserUpdateRequestDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponse toResponse(User user);

    User toUser(UserRegistrationRequestDto userRegistrationRequestDto);

    User toUser(@MappingTarget User user, UserUpdateRequestDto updateRequestDto);

    @Mapping(target = "user.id", ignore = true)
    @Mapping(source = "roleIds", target = "user.roles")
    void toUser(@MappingTarget User user, RoleUpdateRequestDto updateRequestDto);

    @AfterMapping
    default void setRoleIds(@MappingTarget User user, RoleUpdateRequestDto updateRequestDto) {
        if (user.getRoles() == null) {
            user.setRoles(updateRequestDto.roleIds());
        }
    }
}
