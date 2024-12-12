package com.spring.booking.accommodationbookingservice.service.payment.impl;

import static com.spring.booking.accommodationbookingservice.util.Constants.BOOKING_ACCOMMODATION_ID;
import static com.spring.booking.accommodationbookingservice.util.Constants.CORRECT_ACCOMMODATION_ID;
import static com.spring.booking.accommodationbookingservice.util.Constants.CORRECT_BOOKING_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.spring.booking.accommodationbookingservice.domain.Accommodation;
import com.spring.booking.accommodationbookingservice.domain.Booking;
import com.spring.booking.accommodationbookingservice.dto.payment.PaymentCancelResponse;
import com.spring.booking.accommodationbookingservice.dto.payment.PaymentConfirmResponse;
import com.spring.booking.accommodationbookingservice.dto.payment.PaymentCreateRequestDto;
import com.spring.booking.accommodationbookingservice.dto.payment.PaymentResponse;
import com.spring.booking.accommodationbookingservice.mapper.PaymentMapper;
import com.spring.booking.accommodationbookingservice.repository.AccommodationRepository;
import com.spring.booking.accommodationbookingservice.repository.BookingRepository;
import com.spring.booking.accommodationbookingservice.repository.PaymentRepository;
import com.spring.booking.accommodationbookingservice.service.payment.StripeClient;
import com.spring.booking.accommodationbookingservice.telegram.TelegramNotificationMessageBuilder;
import com.spring.booking.accommodationbookingservice.telegram.TelegramNotificationService;
import com.spring.booking.accommodationbookingservice.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {
    @Mock
    private AccommodationRepository accommodationRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private PaymentMapper paymentMapper;

    @Mock
    private StripeClient stripeClient;

    @Mock
    private TelegramNotificationMessageBuilder telegramNotificationMessageBuilder;

    @Mock
    private TelegramNotificationService telegramNotificationService;

    private Accommodation accommodation;
    private Booking booking;
    private PaymentResponse paymentResponse;
    private PaymentCreateRequestDto createRequestDto;
    private PaymentConfirmResponse confirmResponse;
    private PaymentCancelResponse cancelResponse;

    @BeforeEach
    void setUp() {
        accommodation = TestUtil.createAccommodation();

        booking = TestUtil.createBooking();

        paymentResponse = TestUtil.createPaymentResponse();

        createRequestDto = TestUtil.createPaymentRequestDto();

        confirmResponse = TestUtil.createConfirmPaymentResponse();

        cancelResponse = TestUtil.createCancelPaymentResponse();
    }

    @Test
    @DisplayName("Test create() method")
    void create_ValidPaymentCreateRequestDto_ReturnPaymentResponse() {
    }
}