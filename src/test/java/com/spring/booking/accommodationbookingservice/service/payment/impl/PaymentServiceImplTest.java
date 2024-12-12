package com.spring.booking.accommodationbookingservice.service.payment.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.spring.booking.accommodationbookingservice.domain.Payment;
import com.spring.booking.accommodationbookingservice.dto.payment.PaymentCancelResponse;
import com.spring.booking.accommodationbookingservice.dto.payment.PaymentConfirmResponse;
import com.spring.booking.accommodationbookingservice.dto.payment.PaymentCreateRequestDto;
import com.spring.booking.accommodationbookingservice.mapper.PaymentMapper;
import com.spring.booking.accommodationbookingservice.repository.AccommodationRepository;
import com.spring.booking.accommodationbookingservice.repository.BookingRepository;
import com.spring.booking.accommodationbookingservice.repository.PaymentRepository;
import com.spring.booking.accommodationbookingservice.service.payment.StripeClient;
import com.spring.booking.accommodationbookingservice.telegram.TelegramNotificationMessageBuilder;
import com.spring.booking.accommodationbookingservice.telegram.TelegramNotificationService;
import com.spring.booking.accommodationbookingservice.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
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

    private Payment payment;
    private PaymentCreateRequestDto createRequestDto;
    private PaymentConfirmResponse confirmResponse;
    private PaymentCancelResponse cancelResponse;

    @BeforeEach
    void setUp() {
        payment = TestUtil.createPayment();

        createRequestDto = TestUtil.createPaymentRequestDto();

        confirmResponse = TestUtil.createConfirmPaymentResponse();

        cancelResponse = TestUtil.createCancelPaymentResponse();
    }
}