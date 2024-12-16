package com.spring.booking.accommodationbookingservice.service.payment;

import com.spring.booking.accommodationbookingservice.domain.Accommodation;
import com.spring.booking.accommodationbookingservice.domain.Booking;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.param.PaymentIntentCancelParams;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.Period;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeClient {
    private static final String STRIPE_PAYMENT_CURRENCY = "usd";
    private static final String STRIPE_PAYMENT_METHOD = "pm_card_visa";
    private static final String STRIPE_PAYMENT_METHOD_TYPE = "card";
    private static final String STRIPE_PAYMENT_SUCCESS_URL = "https://example.com/success";
    private static final String STRIPE_PAYMENT_CANCEL_URL = "https://example.com/cancel";
    private static final String STRIPE_PAYMENT_PRODUCT_DATA_NAME = "Booking #";
    private static final String STRIPE_PAYMENT_SUCCESS_STATUS = "succeeded";
    private static final String STRIPE_PAYMENT_CANCEL_STATUS_REGEX = "requires_";

    private static final String STRIPE_SESSION_PAYMENT_SUCCESS_STATUS = "paid";

    private static final Long STRIPE_PRODUCT_QUANTITY = 1L;

    private Session stripeSession;
    private PaymentIntent stripePayment;

    @Value("${stripe.key.secret}")
    private String stripeApiKey;

    @PostConstruct
    private void initSecretKey() {
        Stripe.apiKey = stripeApiKey;
    }

    public Session buildStripeSession(Booking booking, Accommodation accommodation)
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
        stripeSession = Session.create(sessionCreateParams);
        initPrePayment();
        return stripeSession;
    }

    public void buildStripeConfirmPayment() throws StripeException {
        PaymentIntentCreateParams createParams = PaymentIntentCreateParams
                .builder()
                .setCurrency(STRIPE_PAYMENT_CURRENCY)
                .setPaymentMethod(STRIPE_PAYMENT_METHOD)
                .setConfirm(true)
                .addPaymentMethodType(STRIPE_PAYMENT_METHOD_TYPE)
                .setAmount(stripeSession.getAmountTotal())
                .build();
        stripePayment = PaymentIntent.create(createParams);
    }

    public boolean isPaymentSuccess() {
        if (stripePayment.getStatus().equals(STRIPE_PAYMENT_SUCCESS_STATUS)) {
            stripeSession.setPaymentIntent(stripePayment.getId());
            stripeSession.setPaymentStatus(STRIPE_SESSION_PAYMENT_SUCCESS_STATUS);
            return true;
        }
        return false;
    }

    public boolean isPaymentCanceled() throws StripeException {
        PaymentIntent resource = PaymentIntent.retrieve(stripePayment.getId());
        if (stripePayment.getStatus().startsWith(STRIPE_PAYMENT_CANCEL_STATUS_REGEX)) {
            PaymentIntentCancelParams params = PaymentIntentCancelParams
                    .builder()
                    .build();
            resource.cancel(params);
            return true;
        }
        return false;
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

    private void initPrePayment() throws StripeException {
        PaymentIntentCreateParams createParams = PaymentIntentCreateParams
                .builder()
                .setCurrency(STRIPE_PAYMENT_CURRENCY)
                .setConfirm(false)
                .setAmount(stripeSession.getAmountTotal())
                .build();
        stripePayment = PaymentIntent.create(createParams);
    }
}
