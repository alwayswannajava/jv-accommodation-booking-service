package com.spring.booking.accommodationbookingservice.service.payment.impl;

import com.spring.booking.accommodationbookingservice.domain.Accommodation;
import com.spring.booking.accommodationbookingservice.domain.Booking;
import com.spring.booking.accommodationbookingservice.domain.Payment;
import com.spring.booking.accommodationbookingservice.domain.enums.Status;
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
import com.spring.booking.accommodationbookingservice.service.payment.PaymentService;
import com.spring.booking.accommodationbookingservice.service.payment.StripeClient;
import com.spring.booking.accommodationbookingservice.telegram.TelegramNotificationMessageBuilder;
import com.spring.booking.accommodationbookingservice.telegram.TelegramNotificationService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {
    private static final String PAYMENT_CONFIRM_MESSAGE = "Your payment was "
            + "successfully confirmed.";
    private static final String PAYMENT_CANCEL_MESSAGE = "You can made it later. "
            + "But keep it in mind, "
            + "that session expires for 24 hours";

    private final AccommodationRepository accommodationRepository;
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final StripeClient stripeClient;
    private final TelegramNotificationMessageBuilder telegramNotificationMessageBuilder;
    private final TelegramNotificationService telegramNotificationService;

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> findAllByUserId(Long userId) {
        return paymentRepository.findAllFetchBookingByUserId(userId)
                .stream()
                .map(paymentMapper::toPaymentResponse)
                .toList();
    }

    @Override
    public PaymentResponse create(PaymentCreateRequestDto createRequestDto) throws StripeException {
        Booking booking = bookingRepository.findById(createRequestDto.bookingId())
                .orElseThrow(() -> new EntityNotFoundException("Booking with id: "
                        + createRequestDto.bookingId()
                        + " not found "));
        Accommodation accommodation = checkAccommodationExist(booking.getAccommodationId());
        Session stripeSession = stripeClient.buildStripeSession(booking, accommodation);
        Payment payment = paymentMapper.toModel(stripeSession);
        payment.setStatus(Status.UNPAID);
        payment.setBookingId(booking.getId());
        return paymentMapper.toPaymentResponse(paymentRepository.save(payment));
    }

    @Override
    public PaymentConfirmResponse confirm(String sessionId)
            throws StripeException {
        stripeClient.buildStripeConfirmPayment();
        Payment payment = paymentRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new EntityNotFoundException("Session with id: "
                        + sessionId
                        + " not found "));
        if (stripeClient.isPaymentSuccess()) {
            payment.setStatus(Status.PAID);
        }
        PaymentConfirmResponse paymentConfirmResponse = new PaymentConfirmResponse(
                PAYMENT_CONFIRM_MESSAGE,
                payment.getBookingId(),
                payment.getStatus(),
                payment.getAmountToPay());
        String builtNotificationMessage = telegramNotificationMessageBuilder
                .buildNotificationMessage(paymentConfirmResponse);
        telegramNotificationService.sendMessage(builtNotificationMessage);
        return paymentConfirmResponse;
    }

    @Override
    public PaymentCancelResponse cancel(String sessionId)
            throws StripeException {
        Payment payment = paymentRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new EntityNotFoundException("Session with id: "
                        + sessionId
                        + " not found "));
        if (stripeClient.isPaymentCanceled()) {
            payment.setStatus(Status.CANCELLED);
        }
        return new PaymentCancelResponse(PAYMENT_CANCEL_MESSAGE);
    }

    private Accommodation checkAccommodationExist(Long accommodationId) {
        return accommodationRepository
                .findByIdFetchAddressAndAmenities(accommodationId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new PaymentProcessingException(
                        "Payment can't be created, because "
                                + "accommodation is not present in our booking system. "
                                + "Maybe it was deleted"
                                + " before. We apologize for the inconvenience"));
    }
}
