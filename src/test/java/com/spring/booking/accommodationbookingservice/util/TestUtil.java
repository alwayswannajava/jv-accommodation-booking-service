package com.spring.booking.accommodationbookingservice.util;

import static com.spring.booking.accommodationbookingservice.util.Constants.ACCOMMODATION_AMENITIES;
import static com.spring.booking.accommodationbookingservice.util.Constants.ACCOMMODATION_AVAILABILITY;
import static com.spring.booking.accommodationbookingservice.util.Constants.ACCOMMODATION_CITY;
import static com.spring.booking.accommodationbookingservice.util.Constants.ACCOMMODATION_COUNTRY;
import static com.spring.booking.accommodationbookingservice.util.Constants.ACCOMMODATION_DAILY_RATE;
import static com.spring.booking.accommodationbookingservice.util.Constants.ACCOMMODATION_POSTAL_CODE;
import static com.spring.booking.accommodationbookingservice.util.Constants.ACCOMMODATION_SIZE;
import static com.spring.booking.accommodationbookingservice.util.Constants.ACCOMMODATION_STREET;
import static com.spring.booking.accommodationbookingservice.util.Constants.ACCOMMODATION_TYPE;
import static com.spring.booking.accommodationbookingservice.util.Constants.CORRECT_ACCOMMODATION_ID;
import static com.spring.booking.accommodationbookingservice.util.Constants.UPDATE_ACCOMMODATION_CITY;
import static com.spring.booking.accommodationbookingservice.util.Constants.UPDATE_ACCOMMODATION_DAILY_RATE;
import static com.spring.booking.accommodationbookingservice.util.Constants.UPDATE_ACCOMMODATION_TYPE;

import com.spring.booking.accommodationbookingservice.domain.Accommodation;
import com.spring.booking.accommodationbookingservice.domain.Amenity;
import com.spring.booking.accommodationbookingservice.dto.accommodation.AccommodationCreateRequestDto;
import com.spring.booking.accommodationbookingservice.dto.accommodation.AccommodationResponse;
import com.spring.booking.accommodationbookingservice.dto.accommodation.AccommodationUpdateRequestDto;
import org.springframework.data.domain.PageRequest;
import java.util.List;

public final class TestUtil {
    private TestUtil() {

    }

    public static AccommodationCreateRequestDto createAccommodationRequestDto() {
        return new AccommodationCreateRequestDto(ACCOMMODATION_TYPE,
                ACCOMMODATION_COUNTRY,
                ACCOMMODATION_CITY,
                ACCOMMODATION_STREET,
                ACCOMMODATION_POSTAL_CODE,
                ACCOMMODATION_SIZE,
                ACCOMMODATION_AMENITIES,
                ACCOMMODATION_DAILY_RATE,
                ACCOMMODATION_AVAILABILITY);
    }

    public static AccommodationUpdateRequestDto createUpdateAccommodationRequestDto() {
        return new AccommodationUpdateRequestDto(UPDATE_ACCOMMODATION_TYPE,
                ACCOMMODATION_COUNTRY,
                UPDATE_ACCOMMODATION_CITY,
                ACCOMMODATION_STREET,
                ACCOMMODATION_POSTAL_CODE,
                ACCOMMODATION_SIZE,
                ACCOMMODATION_AMENITIES,
                UPDATE_ACCOMMODATION_DAILY_RATE,
                ACCOMMODATION_AVAILABILITY);
    }

    public static Accommodation createAccommodation() {
        Accommodation accommodation = new Accommodation();
        accommodation.setId(CORRECT_ACCOMMODATION_ID);
        accommodation.setType(ACCOMMODATION_TYPE);
        accommodation.setSize(ACCOMMODATION_SIZE);
        accommodation.setAvailability(ACCOMMODATION_AVAILABILITY);
        accommodation.setAmenities(List.of(new Amenity("Wi-fi", "Wi-fi technology")));
    }

    public static AccommodationResponse createAccommodationResponse() {
    }

    public static PageRequest createPageRequest() {
        return PageRequest.of(0, 10);
    }
}
