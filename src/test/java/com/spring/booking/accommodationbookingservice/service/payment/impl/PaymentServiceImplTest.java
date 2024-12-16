package com.spring.booking.accommodationbookingservice.service.payment.impl;

import static com.spring.booking.accommodationbookingservice.util.Constants.BOOKING_ACCOMMODATION_ID;
import static com.spring.booking.accommodationbookingservice.util.Constants.CONFIRM_PAYMENT_MESSAGE;
import static com.spring.booking.accommodationbookingservice.util.Constants.CORRECT_ACCOMMODATION_ID;
import static com.spring.booking.accommodationbookingservice.util.Constants.CORRECT_BOOKING_ID;
import static com.spring.booking.accommodationbookingservice.util.Constants.CORRECT_USER_ID;
import static com.spring.booking.accommodationbookingservice.util.Constants.INCORRECT_BOOKING_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.spring.booking.accommodationbookingservice.domain.Accommodation;
import com.spring.booking.accommodationbookingservice.domain.Booking;
import com.spring.booking.accommodationbookingservice.domain.Payment;
import com.spring.booking.accommodationbookingservice.domain.enums.Status;
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
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import java.util.Optional;
import java.util.List;
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
    private Payment payment;
    private PaymentResponse paymentResponse;
    private PaymentCreateRequestDto createRequestDto;
    private PaymentConfirmResponse confirmResponse;
    private PaymentCancelResponse cancelResponse;

    @BeforeEach
    void setUp() {
        accommodation = TestUtil.createAccommodation();

        booking = TestUtil.createBooking();

        paymentResponse = TestUtil.createPaymentResponse();

        payment = TestUtil.createPayment();

        createRequestDto = TestUtil.createPaymentRequestDto();

        confirmResponse = TestUtil.createConfirmPaymentResponse();

        cancelResponse = TestUtil.createCancelPaymentResponse();
    }

    @Test
    @DisplayName("Test findAllByUserId() method")
    void findAllByUserId_ValidUserId_ReturnListPaymentResponse() {
        when(paymentMapper.toPaymentResponse(payment)).thenReturn(paymentResponse);
        when(paymentRepository.findAllFetchBookingByUserId(CORRECT_USER_ID))
                .thenReturn(List.of(payment));

        List<PaymentResponse> expected = List.of(paymentResponse);
        List<PaymentResponse> actual = paymentService.findAllByUserId(CORRECT_USER_ID);

        assertEquals(expected, actual);
        assertEquals(expected.size(), actual.size());

        verify(paymentRepository).findAllFetchBookingByUserId(CORRECT_USER_ID);
        verifyNoMoreInteractions(paymentRepository, paymentMapper);
    }

    @Test
    @DisplayName("Test create() method")
    void create_ValidPaymentCreateRequestDto_ReturnPaymentResponse() throws StripeException {
        when(bookingRepository.findById(CORRECT_BOOKING_ID))
                .thenReturn(Optional.ofNullable(booking));
        when(accommodationRepository.findByIdFetchAddressAndAmenities(CORRECT_ACCOMMODATION_ID))
                .thenReturn(Optional.ofNullable(accommodation));
        Session session = mock(Session.class);
        when(stripeClient.buildStripeSession(booking, accommodation))
                .thenReturn(session);
        when(paymentMapper.toModel(session)).thenReturn(payment);
        when(paymentMapper.toPaymentResponse(payment)).thenReturn(paymentResponse);
        when(paymentRepository.save(payment)).thenReturn(payment);

        paymentService.create(createRequestDto);

        verify(paymentRepository).save(payment);
        verify(paymentMapper).toModel(session);
        verify(paymentMapper).toPaymentResponse(payment);
        verify(stripeClient).buildStripeSession(booking, accommodation);
        verifyNoMoreInteractions(paymentRepository, paymentMapper);
    }

    @Test
    @DisplayName("Test confirm() method")
    void confirm_ValidPayment_ReturnConfirmPaymentResponse() throws StripeException {
        doNothing().when(stripeClient).buildStripeConfirmPayment();
        when(paymentRepository.save(payment)).thenReturn(payment);
        when(stripeClient.isPaymentSuccess()).thenReturn(true);
        when(telegramNotificationMessageBuilder.buildNotificationMessage(confirmResponse))
                .thenReturn(CONFIRM_PAYMENT_MESSAGE);

        paymentService.confirm();

        verify(paymentRepository).save(payment);
        verify(stripeClient).buildStripeConfirmPayment();
        verify(stripeClient).isPaymentSuccess();
        verify(telegramNotificationMessageBuilder).buildNotificationMessage(confirmResponse);
        verify(telegramNotificationService).sendMessage(CONFIRM_PAYMENT_MESSAGE);
        verifyNoMoreInteractions(paymentRepository, stripeClient);
    }

    @Test
    @DisplayName("Test cancel() method")
    void cancel_ValidPayment_ReturnCancelPaymentResponse() throws StripeException {
        when(paymentRepository.save(payment)).thenReturn(payment);
        when(stripeClient.isPaymentCanceled()).thenReturn(false);

        paymentService.cancel();

        verify(paymentRepository).save(payment);
        verify(stripeClient).isPaymentCanceled();
        verifyNoMoreInteractions(paymentRepository, stripeClient);
    }
}