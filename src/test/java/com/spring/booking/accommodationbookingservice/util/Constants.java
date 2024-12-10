package com.spring.booking.accommodationbookingservice.util;

import com.spring.booking.accommodationbookingservice.domain.enums.Type;
import com.spring.booking.accommodationbookingservice.dto.amenity.AmenityDto;
import java.math.BigDecimal;
import java.util.List;

public final class Constants {
    public static final Type ACCOMMODATION_TYPE = Type.CONDO;
    public static final String ACCOMMODATION_COUNTRY = "Ukraine";
    public static final String ACCOMMODATION_CITY = "Kyiv";
    public static final String ACCOMMODATION_STREET = "Mykhailo Grushevskyi Street";
    public static final String ACCOMMODATION_POSTAL_CODE = "01001";
    public static final String ACCOMMODATION_SIZE = "Bedroom 1, Washroom 1";
    public static final List<AmenityDto> ACCOMMODATION_AMENITIES = List.of(new AmenityDto("Wi-fi",
            "Wi-fi technology"));
    public static final BigDecimal ACCOMMODATION_DAILY_RATE = BigDecimal.valueOf(23.23);
    public static final Integer ACCOMMODATION_AVAILABILITY = 10;

    public static final Type UPDATE_ACCOMMODATION_TYPE = Type.APARTMENT;
    public static final String UPDATE_ACCOMMODATION_CITY = "Kharkiv";
    public static final BigDecimal UPDATE_ACCOMMODATION_DAILY_RATE = BigDecimal.valueOf(12.03);

    public static final Long CORRECT_ACCOMMODATION_ID = 1L;


    private Constants() {

    }


}
