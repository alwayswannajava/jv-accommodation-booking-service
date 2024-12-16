package com.spring.booking.accommodationbookingservice.repository;

import static com.spring.booking.accommodationbookingservice.util.Constants.CORRECT_USER_ID;
import static com.spring.booking.accommodationbookingservice.util.Constants.INCORRECT_USER_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.spring.booking.accommodationbookingservice.domain.Payment;
import com.spring.booking.accommodationbookingservice.util.TestUtil;
import java.net.MalformedURLException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PaymentRepositoryTest {
    @Autowired
    private PaymentRepository paymentRepository;

    @Test
    @DisplayName("Test findAllFetchBookingByUserId method()")
    @Sql(scripts = {"classpath:db/scripts/add-booking-test-data.sql",
            "classpath:db/scripts/add-payment-test-data.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:db/scripts/remove-booking-test-data.sql",
            "classpath:db/scripts/remove-payment-test-data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllFetchBookingByUserId_ValidUserId_ReturnListOfPayments()
            throws MalformedURLException {
        List<Payment> expected = TestUtil.createPayments();
        List<Payment> actual = paymentRepository.findAllFetchBookingByUserId(CORRECT_USER_ID);
        assertEquals(expected, actual);
        assertEquals(expected.size(), actual.size());
    }

    @Test
    @DisplayName("Test findAllFetchBookingByUserId method() by non-existent user id")
    void findAllFetchBookingByUserId_InvalidUserId_ReturnEmptyList() {
        List<Payment> actual = paymentRepository.findAllFetchBookingByUserId(INCORRECT_USER_ID);
        assertEquals(0, actual.size());
    }
}
