package com.spring.booking.accommodationbookingservice.dto.role;

import com.spring.booking.accommodationbookingservice.domain.Role;
import jakarta.validation.constraints.NotEmpty;
import java.util.Set;

public record RoleUpdateRequestDto(
        @NotEmpty
        Set<Role> roleIds
) {
}
