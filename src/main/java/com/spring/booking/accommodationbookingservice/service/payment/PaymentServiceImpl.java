package com.spring.booking.accommodationbookingservice.service.payment;

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
import com.spring.booking.accommodationbookingservice.telegram.TelegramNotificationMessageBuilder;
import com.spring.booking.accommodationbookingservice.telegram.TelegramNotificationService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.param.PaymentIntentCancelParams;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private static final String STRIPE_PAYMENT_CURRENCY = "usd";
    private static final String STRIPE_PAYMENT_METHOD = "pm_card_visa";
    private static final String STRIPE_PAYMENT_METHOD_TYPE = "card";
    private static final String STRIPE_PAYMENT_SUCCESS_URL = "https://example.com/success";
    private static final String STRIPE_PAYMENT_CANCEL_URL = "https://example.com/cancel";
    private static final String STRIPE_PAYMENT_CONFIRM_MESSAGE = "Your payment was "
            + "successfully confirmed.";
    private static final String STRIPE_PAYMENT_CANCEL_MESSAGE = ". You can made it later. "
            + "But keep it in mind, "
            + "that session expires for 24 hours";
    private static final String STRIPE_PAYMENT_PRODUCT_DATA_NAME = "Booking #";
    private static final String STRIPE_PAYMENT_SUCCESS_STATUS = "succeeded";
    private static final String STRIPE_PAYMENT_CANCEL_STATUS_REGEX = "requires_";

    private static final String STRIPE_SESSION_PAYMENT_SUCCESS_STATUS = "paid";

    private static final Long STRIPE_PRODUCT_QUANTITY = 1L;

    private final AccommodationRepository accommodationRepository;
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final TelegramNotificationMessageBuilder telegramNotificationMessageBuilder;
    private final TelegramNotificationService telegramNotificationService;

    @Value("${stripe.key.secret}")
    private String stripeApiKey;

    private Session stripeSession;
    private Payment payment;
    private PaymentIntent stripePayment;

    @PostConstruct
    private void initSecretKey() {
        Stripe.apiKey = stripeApiKey;
    }

    @Override
    public List<PaymentResponse> findAllByUserId(Long userId) {
        return paymentRepository.findAllByUserId(userId)
                .stream()
                .map(paymentMapper::toPaymentResponse)
                .toList();
    }

    @Override
    @Transactional
    public PaymentResponse create(PaymentCreateRequestDto createRequestDto) throws StripeException {
        Booking booking = bookingRepository.findById(createRequestDto.bookingId())
                .orElseThrow(() -> new EntityNotFoundException("Booking with id: "
                        + createRequestDto.bookingId()
                        + " not found "));
        Accommodation accommodation = checkAccommodationExist(booking.getAccommodationId());
        stripeSession = buildStripeSession(booking, accommodation);
        payment = paymentMapper.toModel(stripeSession);
        payment.setStatus(Status.UNPAID);
        payment.setBookingId(booking.getId());
        return paymentMapper.toPaymentResponse(paymentRepository.save(payment));
    }

    @Override
    @Transactional
    public PaymentConfirmResponse confirm() throws StripeException {
        stripePayment = buildStripeConfirmPayment();
        if (stripePayment.getStatus().equals(STRIPE_PAYMENT_SUCCESS_STATUS)) {
            stripeSession.setPaymentIntent(stripePayment.getId());
            stripeSession.setPaymentStatus(STRIPE_SESSION_PAYMENT_SUCCESS_STATUS);
            payment.setStatus(Status.PAID);
            paymentRepository.save(payment);
        }
        PaymentConfirmResponse paymentConfirmResponse = new PaymentConfirmResponse(
                STRIPE_PAYMENT_CONFIRM_MESSAGE,
                payment.getSessionUrl(),
                payment.getSessionId(),
                payment.getStatus(),
                payment.getAmountToPay());
        String builtNotificationMessage = telegramNotificationMessageBuilder
                .buildNotificationMessage(paymentConfirmResponse);
        telegramNotificationService.sendMessage(builtNotificationMessage);
        return paymentConfirmResponse;
    }

    @Override
    @Transactional
    public PaymentCancelResponse cancel() throws StripeException {
        PaymentIntent resource = PaymentIntent.retrieve(stripePayment.getId());
        if (stripePayment.toString().startsWith(STRIPE_PAYMENT_CANCEL_STATUS_REGEX)) {
            PaymentIntentCancelParams params = PaymentIntentCancelParams
                    .builder()
                    .build();

            resource.cancel(params);
            payment.setStatus(Status.CANCELLED);
            paymentRepository.save(payment);
        }
        return new PaymentCancelResponse(STRIPE_PAYMENT_CANCEL_MESSAGE);
    }

    private Long calculateAmount(Booking booking, Accommodation accommodation) {
        int daysDifference = calculateDifferenceBetweenTwoDatesInDays(
                booking.getCheckInDate(),
                booking.getCheckOutDate());
        return daysDifference * accommodation.getDailyRate().longValue();
    }

    private int calculateDifferenceBetweenTwoDatesInDays(
            LocalDate checkInDate, LocalDate checkOutDate) {
        return Period.between(checkInDate, checkOutDate).getDays();
    }

    private Session buildStripeSession(Booking booking, Accommodation accommodation)
            throws StripeException {
        SessionCreateParams sessionCreateParams = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(STRIPE_PAYMENT_SUCCESS_URL)
                .setCancelUrl(STRIPE_PAYMENT_CANCEL_URL)
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(STRIPE_PRODUCT_QUANTITY)
                        .setPriceData(
                                SessionCreateParams.LineItem.PriceData.builder()
                                        .setCurrency(STRIPE_PAYMENT_CURRENCY)
                                        .setUnitAmount(calculateAmount(booking, accommodation))
                                        .setProductData(SessionCreateParams.LineItem
                                                .PriceData.ProductData.builder()
                                                .setName(STRIPE_PAYMENT_PRODUCT_DATA_NAME
                                                        + booking.getId())
                                                .build())
                                        .build())
                        .build())
                .build();
        return Session.create(sessionCreateParams);
    }

    private PaymentIntent buildStripeConfirmPayment() throws StripeException {
        PaymentIntentCreateParams createParams = PaymentIntentCreateParams
                .builder()
                .setCurrency(STRIPE_PAYMENT_CURRENCY)
                .setPaymentMethod(STRIPE_PAYMENT_METHOD)
                .setConfirm(true)
                .addPaymentMethodType(STRIPE_PAYMENT_METHOD_TYPE)
                .setAmount(stripeSession.getAmountTotal())
                .build();
        return PaymentIntent.create(createParams);
    }

    private Accommodation checkAccommodationExist(Long accommodationId) {
        return accommodationRepository
                .findById(accommodationId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new PaymentProcessingException("Payment can't be done, because "
                        + "accommodation is not present in our booking system. Maybe it was deleted"
                        + " before. We apologize for the inconvenience"));
    }
}
