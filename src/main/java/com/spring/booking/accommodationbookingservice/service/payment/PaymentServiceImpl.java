package com.spring.booking.accommodationbookingservice.service.payment;

import com.spring.booking.accommodationbookingservice.domain.Accommodation;
import com.spring.booking.accommodationbookingservice.domain.Booking;
import com.spring.booking.accommodationbookingservice.domain.Payment;
import com.spring.booking.accommodationbookingservice.domain.enums.Status;
import com.spring.booking.accommodationbookingservice.dto.payment.PaymentCreateRequestDto;
import com.spring.booking.accommodationbookingservice.dto.payment.PaymentResponse;
import com.spring.booking.accommodationbookingservice.exception.EntityNotFoundException;
import com.spring.booking.accommodationbookingservice.mapper.PaymentMapper;
import com.spring.booking.accommodationbookingservice.repository.AccommodationRepository;
import com.spring.booking.accommodationbookingservice.repository.BookingRepository;
import com.spring.booking.accommodationbookingservice.repository.PaymentRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private static final String PAYMENT_CURRENCY = "usd";
    private static final String PAYMENT_SUCCESS_URL = "https://example.com/success";
    private static final String PAYMENT_CANCEL_URL = "https://example.com/cancel";
    private static final Long PRODUCT_QUANTITY = 1L;

    private final AccommodationRepository accommodationRepository;
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    @Value("${stripe.key.secret}")
    private String stripeApiKey;

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
        Stripe.apiKey = stripeApiKey;
        Booking booking = bookingRepository.findById(createRequestDto.bookingId())
                .orElseThrow(() -> new EntityNotFoundException("Booking with id: "
                        + createRequestDto.bookingId()
                        + " not found "));
        Accommodation accommodation = accommodationRepository.findById(booking.getAccommodationId()).get();
        Session session = buildStripeSessionModel(booking, accommodation);
        Payment payment = paymentMapper.toModel(session);
        payment.setStatus(Status.UNPAID);
        payment.setBookingId(booking.getId());
        return paymentMapper.toPaymentResponse(paymentRepository.save(payment));
    }

    private Long calculateAmount(Booking booking, Accommodation accommodation) {
        int daysDifference = calculateDifferenceBetweenTwoDatesInDays(booking.getCheckInDate(),
                booking.getCheckOutDate());
        return daysDifference * accommodation.getDailyRate().longValue();
    }

    private int calculateDifferenceBetweenTwoDatesInDays(LocalDate checkInDate, LocalDate checkOutDate) {
        return Period.between(checkInDate, checkOutDate).getDays();
    }

    private Session buildStripeSessionModel(Booking booking, Accommodation accommodation)
            throws StripeException {
        SessionCreateParams sessionCreateParams = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(PAYMENT_SUCCESS_URL)
                .setCancelUrl(PAYMENT_CANCEL_URL)
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(PRODUCT_QUANTITY)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency(PAYMENT_CURRENCY)
                                                .setUnitAmount(calculateAmount(booking, accommodation))
                                                .setProductData(SessionCreateParams.LineItem.
                                                         PriceData.ProductData.builder()
                                                        .setName("Booking #" + booking.getId())
                                                        .build())
                                                .build())
                                .build())
                .build();
        return Session.create(sessionCreateParams);
    }
}
