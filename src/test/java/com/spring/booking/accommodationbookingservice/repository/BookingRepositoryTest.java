package com.spring.booking.accommodationbookingservice.repository;

import static com.spring.booking.accommodationbookingservice.util.Constants.CORRECT_ACCOMMODATION_ID;
import static com.spring.booking.accommodationbookingservice.util.Constants.CORRECT_USER_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.spring.booking.accommodationbookingservice.domain.Booking;
import com.spring.booking.accommodationbookingservice.domain.enums.Status;
import com.spring.booking.accommodationbookingservice.util.TestUtil;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookingRepositoryTest {
    @Autowired
    private BookingRepository bookingRepository;

    private List<Booking> expectedBookingList;

    @BeforeEach
    void setUp() {
        expectedBookingList = TestUtil.createBookings();
    }

    @Test
    @DisplayName("Test findByUserIdAndStatus() method")
    @Sql(scripts = "classpath:db/scripts/add-booking-test-data.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:db/scripts/remove-booking-test-data.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByUserIdAndStatus_ValidData_ReturnListOfBookings() {
        List<Booking> actual = bookingRepository.findByUserIdAndStatus(CORRECT_USER_ID, Status.PENDING);
        Collections.reverse(actual);
        assertEquals(expectedBookingList, actual);
        assertEquals(expectedBookingList.size(), actual.size());
    }

    @Test
    @DisplayName("Test findAllByUserId() method")
    @Sql(scripts = "classpath:db/scripts/add-booking-test-data.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:db/scripts/remove-booking-test-data.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllByUserId_ValidUserId_ReturnListOfBookings() {
        List<Booking> actual = bookingRepository.findAllByUserId(CORRECT_USER_ID);
        assertEquals(expectedBookingList, actual);
        assertEquals(expectedBookingList.size(), actual.size());
    }

    @Test
    @DisplayName("Test findAllByStatus() method")
    @Sql(scripts = "classpath:db/scripts/add-booking-test-data.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:db/scripts/remove-booking-test-data.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllByStatus_ValidStatus_ReturnListOfBookings() {
        List<Booking> actual = bookingRepository.findAllByStatus(Status.PENDING);
        assertEquals(expectedBookingList, actual);
        assertEquals(expectedBookingList.size(), actual.size());
    }

    @Test
    @DisplayName("Test findAllByStatus() method by non-existent status")
    void findAllByStatus_InvalidStatus_ReturnEmptyList() {
        List<Booking> actual = bookingRepository.findAllByStatus(Status.PAID);
        assertEquals(0, actual.size());
    }

    @Test
    @DisplayName("Test findByAccommodationIdAndCheckInAndCheckOutDate() method")
    @Sql(scripts = "classpath:db/scripts/add-booking-test-data.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:db/scripts/remove-booking-test-data.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllAccommodationIdAndCheckInAndCheckOutDate_ValidData_ReturnOptional() {
        Optional<Booking> expectedBooking = Optional.of(expectedBookingList.get(0));
        Optional<Booking> actualBooking = bookingRepository
                .findByAccommodationIdAndCheckInDateAndCheckOutDate(CORRECT_ACCOMMODATION_ID,
                        LocalDate.of(2024, 12, 18),
                        LocalDate.of(2024, 12, 24)
        );
        assertEquals(expectedBooking.get(), actualBooking.get());
    }
}