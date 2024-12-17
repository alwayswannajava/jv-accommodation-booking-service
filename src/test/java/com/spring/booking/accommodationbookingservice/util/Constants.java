package com.spring.booking.accommodationbookingservice.util;

import com.spring.booking.accommodationbookingservice.domain.Amenity;
import com.spring.booking.accommodationbookingservice.domain.enums.Type;
import com.spring.booking.accommodationbookingservice.dto.amenity.AmenityDto;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public final class Constants {
    public static final Type ACCOMMODATION_TYPE = Type.CONDO;
    public static final String ACCOMMODATION_COUNTRY = "Ukraine";
    public static final String ACCOMMODATION_CITY = "Kyiv";
    public static final String ACCOMMODATION_STREET = "Lesyi Ukrainki Street";
    public static final String ACCOMMODATION_POSTAL_CODE = "01001";
    public static final String ACCOMMODATION_SIZE = "Bedroom 1, Washroom 1";
    public static final List<AmenityDto> ACCOMMODATION_AMENITIES_DTO = List.of(new AmenityDto(
            "Wi-fi", "Wi-fi technology"));
    public static final List<Amenity> ACCOMMODATION_AMENITIES = List.of(new Amenity(
            "Wi-fi", "Wi-fi technology"));
    public static final BigDecimal ACCOMMODATION_DAILY_RATE = BigDecimal.valueOf(1123.23);
    public static final Integer ACCOMMODATION_AVAILABILITY = 10;

    public static final Type UPDATE_ACCOMMODATION_TYPE = Type.APARTMENT;
    public static final String UPDATE_ACCOMMODATION_CITY = "Kharkiv";
    public static final BigDecimal UPDATE_ACCOMMODATION_DAILY_RATE = BigDecimal.valueOf(12.03);

    public static final Long CORRECT_ACCOMMODATION_ID = 1L;
    public static final Long INCORRECT_ACCOMMODATION_ID = 1000L;
    public static final Long CORRECT_LOCATION_ID = 1L;

    public static final String EXPECTED_NOT_FOUND_ACCOMMODATION_ENTITY_MESSAGE = "Accommodation "
            + "with id: "
            + INCORRECT_ACCOMMODATION_ID
            + " not found ";

    public static final String TELEGRAM_NOTIFICATION_CREATE_ACCOMMODATION_MESSAGE = """
            ✅Available accommodation:
            🆔ID: 1
            🛌Type: CONDO
            🌎Location: country=Ukraine, city=Kyiv, street=Lesyi Ukrainki Street, postalCode=01001
            🏠Size: Bedroom 1, Washroom 1
            📦Amenities: [AmenityDto[title=Wi-fi, description=Wi-fi technology]]
            💶DailyRate: 23.23
            🟢Availability: 10""";

    public static final String TELEGRAM_NOTIFICATION_CREATE_BOOKING_MESSAGE = """
            🏪Your booking:
            🗓Check-In-Date: 2024-12-16
            🗓Check-Out-Date: 2024-12-18
            🆔Accommodation ID: 1
            🌐User ID: 1
            ✅Status: PENDING""";

    public static final String EXPECTED_ACCOMMODATION_CANCEL_TWICE_MESSAGE = "Cannot cancel "
            + "accommodation: "
            + CORRECT_ACCOMMODATION_ID
            + " twice";

    public static final Long CORRECT_BOOKING_ID = 1L;
    public static final Long CORRECT_USER_ID = 1L;
    public static final Long INCORRECT_USER_ID = 1000L;
    public static final Long INCORRECT_BOOKING_ID = 1000L;
    public static final LocalDate BOOKING_CHECK_IN_DATE = LocalDate.of(24, 12, 16);
    public static final LocalDate BOOKING_CHECK_OUT_DATE = LocalDate.of(24, 12, 18);
    public static final Long BOOKING_ACCOMMODATION_ID = 1L;

    public static final LocalDate UPDATE_BOOKING_CHECK_IN_DATE = LocalDate.of(24, 12, 22);
    public static final LocalDate UPDATE_BOOKING_CHECK_OUT_DATE = LocalDate.of(24, 12, 24);

    public static final String EXPECTED_NOT_FOUND_BOOKING_ENTITY_MESSAGE = "Can't find "
            + "any bookings with bookingId: "
            + INCORRECT_BOOKING_ID;
    public static final String EXPECTED_NOT_FOUND_BOOKING_PAYMENT_ENTITY_MESSAGE =
            "Can't find any bookings with bookingId: " + CORRECT_BOOKING_ID;
    public static final Long CORRECT_PAYMENT_ID = 1L;
    public static final String CORRECT_PAYMENT_URL = "https://checkout.stripe.com/c/pay/cs_test_a194p"
            + "Cj40l2l67Gvyt94XCTjZ3w2XZxTUtC47wqq5LIglgjiG2B4TxX4Dz#fi";
    public static final String CORRECT_SESSION_ID = "cs_test_a1LbRYkF36pyppi7C0KJNuzgwOKm";
    public static final String SUCCESS_PAYMENT_MESSAGE = "Your payment was "
            + "successfully confirmed.";
    public static final String CANCEL_PAYMENT_MESSAGE = "You can made it later. "
            + "But keep it in mind, "
            + "that session expires for 24 hours";
    public static final String CONFIRM_PAYMENT_MESSAGE = "";
    public static final String INCORRECT_SESSION_ID = "cs_test_12312???das,ca";

    public static final String CORRECT_USER_EMAIL = "ivan@gmail.com";
    public static final String CORRECT_USER_PASSWORD = "strongPassword";
    public static final String CORRECT_USER_REPEAT_PASSWORD = "strongPassword";
    public static final String CORRECT_USER_FIRST_NAME = "Ivan";
    public static final String CORRECT_USER_LAST_NAME = "Mazepa";

    public static final Long CORRECT_ROLE_ID = 1L;

    public static final String UPDATE_USER_PASSWORD = "updatePassword";
    public static final String UPDATE_USER_REPEAT_PASSWORD = "updatePassword";
    public static final String UPDATE_FIRST_NAME = "Bogdan";

    public static final String EXPECTED_DUPLICATE_EMAIL_USER_REGISTER_MESSAGE =
            "Can't register user, duplicate email";
    public static final String EXPECTED_NOT_FOUND_SESSION_MESSAGE = "Session with id: "
            + INCORRECT_SESSION_ID
            + " not found ";
    public static final String ENCODED_USER_PASSWORD = "$2a$10$YJU0n0xMnO8YShyAT41c"
            + "TunvtEcQgQc59ZPRrRRokRAv4dj6mh2vC";
    public static final String UPDATED_ENCODED_USER_PASSWORD = "$2a$10$54qQo13hTcXxp"
            + "J6UF91D7esMgmQll9YkZAAfd13tUkFRnCbcIsFM2";

    public static final String ACCOMMODATION_URL = "/accommodations";
    private Constants() {
    }
}

