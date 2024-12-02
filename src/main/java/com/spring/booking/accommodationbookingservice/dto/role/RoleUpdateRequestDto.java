package com.spring.booking.accommodationbookingservice.dto.role;

import com.spring.booking.accommodationbookingservice.domain.enums.RoleName;
import jakarta.validation.constraints.NotEmpty;
import java.util.Set;

public record RoleUpdateRequestDto(
        @NotEmpty
        Set<RoleName> roles
) {
}
