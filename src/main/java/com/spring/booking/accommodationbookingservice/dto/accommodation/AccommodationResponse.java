package com.spring.booking.accommodationbookingservice.dto.accommodation;

import com.spring.booking.accommodationbookingservice.domain.Location;
import com.spring.booking.accommodationbookingservice.domain.enums.Type;
import com.spring.booking.accommodationbookingservice.dto.amenity.AmenityDto;
import java.math.BigDecimal;
import java.util.List;

public record AccommodationResponse(
        Long id,
        Type type,
        Location address,
        String size,
        List<AmenityDto> amenities,
        BigDecimal dailyRate,
        Integer availability
) {
}
