package com.spring.booking.accommodationbookingservice.util;

import static com.spring.booking.accommodationbookingservice.util.Constants.ACCOMMODATION_AMENITIES;
import static com.spring.booking.accommodationbookingservice.util.Constants.ACCOMMODATION_AMENITIES_DTO;
import static com.spring.booking.accommodationbookingservice.util.Constants.ACCOMMODATION_AVAILABILITY;
import static com.spring.booking.accommodationbookingservice.util.Constants.ACCOMMODATION_CITY;
import static com.spring.booking.accommodationbookingservice.util.Constants.ACCOMMODATION_COUNTRY;
import static com.spring.booking.accommodationbookingservice.util.Constants.ACCOMMODATION_DAILY_RATE;
import static com.spring.booking.accommodationbookingservice.util.Constants.ACCOMMODATION_POSTAL_CODE;
import static com.spring.booking.accommodationbookingservice.util.Constants.ACCOMMODATION_SIZE;
import static com.spring.booking.accommodationbookingservice.util.Constants.ACCOMMODATION_STREET;
import static com.spring.booking.accommodationbookingservice.util.Constants.ACCOMMODATION_TYPE;
import static com.spring.booking.accommodationbookingservice.util.Constants.BOOKING_ACCOMMODATION_ID;
import static com.spring.booking.accommodationbookingservice.util.Constants.BOOKING_CHECK_IN_DATE;
import static com.spring.booking.accommodationbookingservice.util.Constants.BOOKING_CHECK_OUT_DATE;
import static com.spring.booking.accommodationbookingservice.util.Constants.CANCEL_PAYMENT_MESSAGE;
import static com.spring.booking.accommodationbookingservice.util.Constants.CORRECT_ACCOMMODATION_ID;
import static com.spring.booking.accommodationbookingservice.util.Constants.CORRECT_BOOKING_ID;
import static com.spring.booking.accommodationbookingservice.util.Constants.CORRECT_LOCATION_ID;
import static com.spring.booking.accommodationbookingservice.util.Constants.CORRECT_PAYMENT_ID;
import static com.spring.booking.accommodationbookingservice.util.Constants.CORRECT_PAYMENT_URL;
import static com.spring.booking.accommodationbookingservice.util.Constants.CORRECT_SESSION_ID;
import static com.spring.booking.accommodationbookingservice.util.Constants.CORRECT_USER_ID;
import static com.spring.booking.accommodationbookingservice.util.Constants.SUCCESS_PAYMENT_MESSAGE;
import static com.spring.booking.accommodationbookingservice.util.Constants.UPDATE_ACCOMMODATION_CITY;
import static com.spring.booking.accommodationbookingservice.util.Constants.UPDATE_ACCOMMODATION_DAILY_RATE;
import static com.spring.booking.accommodationbookingservice.util.Constants.UPDATE_ACCOMMODATION_TYPE;
import static com.spring.booking.accommodationbookingservice.util.Constants.UPDATE_BOOKING_CHECK_IN_DATE;
import static com.spring.booking.accommodationbookingservice.util.Constants.UPDATE_BOOKING_CHECK_OUT_DATE;

import com.spring.booking.accommodationbookingservice.domain.Accommodation;
import com.spring.booking.accommodationbookingservice.domain.Booking;
import com.spring.booking.accommodationbookingservice.domain.Location;
import com.spring.booking.accommodationbookingservice.domain.Payment;
import com.spring.booking.accommodationbookingservice.domain.enums.Status;
import com.spring.booking.accommodationbookingservice.dto.accommodation.AccommodationCreateRequestDto;
import com.spring.booking.accommodationbookingservice.dto.accommodation.AccommodationResponse;
import com.spring.booking.accommodationbookingservice.dto.accommodation.AccommodationUpdateRequestDto;
import com.spring.booking.accommodationbookingservice.dto.booking.BookingCreateRequestDto;
import com.spring.booking.accommodationbookingservice.dto.booking.BookingResponse;
import com.spring.booking.accommodationbookingservice.dto.booking.BookingUpdateRequestDto;
import com.spring.booking.accommodationbookingservice.dto.payment.PaymentCancelResponse;
import com.spring.booking.accommodationbookingservice.dto.payment.PaymentConfirmResponse;
import com.spring.booking.accommodationbookingservice.dto.payment.PaymentCreateRequestDto;
import lombok.SneakyThrows;
import org.springframework.data.domain.PageRequest;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;

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
                ACCOMMODATION_AMENITIES_DTO,
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
                ACCOMMODATION_AMENITIES_DTO,
                UPDATE_ACCOMMODATION_DAILY_RATE,
                ACCOMMODATION_AVAILABILITY);
    }

    public static Accommodation createAccommodation() {
        Accommodation accommodation = new Accommodation();
        Location address = createAddress();
        accommodation.setId(CORRECT_ACCOMMODATION_ID);
        accommodation.setAddress(address);
        accommodation.setType(ACCOMMODATION_TYPE);
        accommodation.setSize(ACCOMMODATION_SIZE);
        accommodation.setAvailability(ACCOMMODATION_AVAILABILITY);
        accommodation.setAmenities(ACCOMMODATION_AMENITIES);
        accommodation.setDailyRate(ACCOMMODATION_DAILY_RATE);
        return accommodation;
    }

    public static AccommodationResponse createAccommodationResponse() {
        return new AccommodationResponse(CORRECT_ACCOMMODATION_ID,
                ACCOMMODATION_TYPE,
                createAddress(),
                ACCOMMODATION_SIZE,
                ACCOMMODATION_AMENITIES_DTO,
                ACCOMMODATION_DAILY_RATE,
                ACCOMMODATION_AVAILABILITY);
    }

    public static AccommodationResponse createUpdateAccommodationResponse() {
        return new AccommodationResponse(CORRECT_ACCOMMODATION_ID,
                ACCOMMODATION_TYPE,
                createAddress(),
                ACCOMMODATION_SIZE,
                ACCOMMODATION_AMENITIES_DTO,
                ACCOMMODATION_DAILY_RATE,
                ACCOMMODATION_AVAILABILITY);
    }

    public static PageRequest createPageRequest() {
        return PageRequest.of(0, 10);
    }

    private static Location createAddress() {
        Location location = new Location();
        location.setId(CORRECT_LOCATION_ID);
        location.setCity(ACCOMMODATION_CITY);
        location.setCountry(ACCOMMODATION_COUNTRY);
        location.setStreet(ACCOMMODATION_STREET);
        location.setPostalCode(ACCOMMODATION_POSTAL_CODE);
        return location;
    }

    public static BookingCreateRequestDto createBookingRequestDto() {
        return new BookingCreateRequestDto(BOOKING_CHECK_IN_DATE,
                BOOKING_CHECK_OUT_DATE,
                BOOKING_ACCOMMODATION_ID);
    }

    public static BookingUpdateRequestDto createBookingUpdateRequestDto() {
        return new BookingUpdateRequestDto(UPDATE_BOOKING_CHECK_IN_DATE,
                UPDATE_BOOKING_CHECK_OUT_DATE,
                BOOKING_ACCOMMODATION_ID);
    }

    public static Booking createBooking() {
        Booking booking = new Booking();
        booking.setId(CORRECT_BOOKING_ID);
        booking.setCheckInDate(BOOKING_CHECK_IN_DATE);
        booking.setCheckOutDate(BOOKING_CHECK_OUT_DATE);
        booking.setAccommodationId(BOOKING_ACCOMMODATION_ID);
        booking.setStatus(Status.PENDING);
        booking.setUserId(CORRECT_USER_ID);
        return booking;
    }

    public static BookingResponse createBookingResponse() {
        return new BookingResponse(BOOKING_CHECK_IN_DATE,
                BOOKING_CHECK_OUT_DATE,
                BOOKING_ACCOMMODATION_ID,
                CORRECT_USER_ID,
                Status.PENDING);
    }

    public static BookingResponse createUpdateBookingResponse() {
        return new BookingResponse(UPDATE_BOOKING_CHECK_IN_DATE,
                UPDATE_BOOKING_CHECK_OUT_DATE,
                BOOKING_ACCOMMODATION_ID,
                CORRECT_USER_ID,
                Status.PENDING);
    }

    @SneakyThrows
    public static Payment createPayment() {
        Payment payment = new Payment();
        payment.setId(CORRECT_PAYMENT_ID);
        payment.setBookingId(CORRECT_BOOKING_ID);
        payment.setStatus(Status.UNPAID);
        payment.setSessionUrl(new URL(CORRECT_PAYMENT_URL));
        payment.setAmountToPay(BigDecimal.valueOf(800));
        payment.setSessionId(CORRECT_SESSION_ID);
        return payment;
    }

    public static PaymentCreateRequestDto createPaymentRequestDto() {
        return new PaymentCreateRequestDto(CORRECT_BOOKING_ID);
    }

    @SneakyThrows
    public static PaymentConfirmResponse createConfirmPaymentResponse() {
        return new PaymentConfirmResponse(SUCCESS_PAYMENT_MESSAGE,
                new URL(CORRECT_PAYMENT_URL),
                CORRECT_SESSION_ID,
                Status.PAID,
                BigDecimal.valueOf(800));
    }

    public static PaymentCancelResponse createCancelPaymentResponse() {
        return new PaymentCancelResponse(CANCEL_PAYMENT_MESSAGE);
    }
}