package com.spring.booking.accommodationbookingservice.service.payment.impl;

import static com.spring.booking.accommodationbookingservice.util.Constants.CONFIRM_PAYMENT_MESSAGE;
import static com.spring.booking.accommodationbookingservice.util.Constants.CORRECT_ACCOMMODATION_ID;
import static com.spring.booking.accommodationbookingservice.util.Constants.CORRECT_BOOKING_ID;
import static com.spring.booking.accommodationbookingservice.util.Constants.CORRECT_SESSION_ID;
import static com.spring.booking.accommodationbookingservice.util.Constants.CORRECT_USER_ID;
import static com.spring.booking.accommodationbookingservice.util.Constants.EXPECTED_NOT_FOUND_BOOKING_ENTITY_MESSAGE;
import static com.spring.booking.accommodationbookingservice.util.Constants.EXPECTED_NOT_FOUND_BOOKING_PAYMENT_ENTITY_MESSAGE;
import static com.spring.booking.accommodationbookingservice.util.Constants.EXPECTED_NOT_FOUND_SESSION_MESSAGE;
import static com.spring.booking.accommodationbookingservice.util.Constants.INCORRECT_SESSION_ID;
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
import com.spring.booking.accommodationbookingservice.dto.payment.PaymentCancelResponse;
import com.spring.booking.accommodationbookingservice.dto.payment.PaymentConfirmResponse;
import com.spring.booking.accommodationbookingservice.dto.payment.PaymentCreateRequestDto;
import com.spring.booking.accommodationbookingservice.dto.payment.PaymentResponse;
import com.spring.booking.accommodationbookingservice.exception.EntityNotFoundException;
import com.spring.booking.accommodationbookingservice.exception.PaymentProcessingException;
import com.spring.booking.accommodationbookingservice.mapper.PaymentMapper;
import com.spring.booking.accommodationbookingservice.repository.AccommodationRepository;
import com.spring.booking.accommodationbookingservice.repository.BookingRepository;
import com.spring.booking.accommodationbookingservice.repository.PaymentRepository;
import com.spring.booking.accommodationbookingservice.service.payment.StripeClient;
import com.spring.booking.accommodationbookingservice.telegram.NotificationMessageBuilder;
import com.spring.booking.accommodationbookingservice.telegram.TelegramNotificationService;
import com.spring.booking.accommodationbookingservice.util.TestUtil;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import java.util.List;
import java.util.Optional;
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
    private NotificationMessageBuilder notificationMessageBuilder;

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

        PaymentResponse expected = paymentResponse;
        PaymentResponse actual = paymentService.create(createRequestDto);
        assertEquals(expected, actual);

        verify(paymentRepository).save(payment);
        verify(paymentMapper).toModel(session);
        verify(paymentMapper).toPaymentResponse(payment);
        verify(stripeClient).buildStripeSession(booking, accommodation);
        verifyNoMoreInteractions(paymentRepository, paymentMapper);
    }

    @Test
    @DisplayName("Test create() method by non-existent booking")
    void create_InvalidBooking_ThrowEntityNotFoundException() {
        when(bookingRepository.findById(createRequestDto.bookingId()))
                .thenReturn(Optional.empty());
        EntityNotFoundException entityNotFoundException = assertThrows(
                EntityNotFoundException.class,
                () -> paymentService.create(createRequestDto)
        );

        String actualMessage = entityNotFoundException.getMessage();
        assertEquals(EXPECTED_NOT_FOUND_BOOKING_PAYMENT_ENTITY_MESSAGE, actualMessage);

        verify(bookingRepository).findById(createRequestDto.bookingId());
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    @DisplayName("Test confirm() method by non-existent session")
    void confirm_InvalidBooking_ThrowEntityNotFoundException() {
        when(paymentRepository.findBySessionId(INCORRECT_SESSION_ID))
                .thenReturn(Optional.empty());
        PaymentProcessingException paymentProcessingException = assertThrows(
                PaymentProcessingException.class,
                () -> paymentService.confirm(INCORRECT_SESSION_ID)
        );

        String actualMessage = paymentProcessingException.getMessage();
        assertEquals(EXPECTED_NOT_FOUND_SESSION_MESSAGE, actualMessage);

        verify(paymentRepository).findBySessionId(INCORRECT_SESSION_ID);
        verifyNoMoreInteractions(paymentRepository);
    }

    @Test
    @DisplayName("Test confirm() method")
    void confirm_ValidPayment_ReturnConfirmPaymentResponse() throws StripeException {
        doNothing().when(stripeClient).buildStripeConfirmPayment();
        when(stripeClient.isPaymentSuccess()).thenReturn(true);
        when(paymentRepository.findBySessionId(CORRECT_SESSION_ID))
                .thenReturn(Optional.ofNullable(payment));
        when(notificationMessageBuilder.buildNotificationMessage(confirmResponse))
                .thenReturn(CONFIRM_PAYMENT_MESSAGE);

        PaymentConfirmResponse expected = confirmResponse;
        PaymentConfirmResponse actual = paymentService.confirm(CORRECT_SESSION_ID);
        assertEquals(expected, actual);

        verify(stripeClient).buildStripeConfirmPayment();
        verify(stripeClient).isPaymentSuccess();
        verify(notificationMessageBuilder).buildNotificationMessage(confirmResponse);
        verify(telegramNotificationService).sendMessage(CONFIRM_PAYMENT_MESSAGE);
        verifyNoMoreInteractions(paymentRepository, stripeClient);
    }

    @Test
    @DisplayName("Test cancel() method")
    void cancel_ValidPayment_ReturnCancelPaymentResponse() throws StripeException {
        when(paymentRepository.findBySessionId(CORRECT_SESSION_ID))
                .thenReturn(Optional.ofNullable(payment));
        when(stripeClient.isPaymentCanceled()).thenReturn(false);

        PaymentCancelResponse expected = cancelResponse;
        PaymentCancelResponse actual = paymentService.cancel(CORRECT_SESSION_ID);
        assertEquals(expected, actual);

        verify(stripeClient).isPaymentCanceled();
        verifyNoMoreInteractions(paymentRepository, stripeClient);
    }
}
