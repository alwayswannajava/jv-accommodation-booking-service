package com.spring.booking.accommodationbookingservice.service.payment;

import com.spring.booking.accommodationbookingservice.domain.Accommodation;
import com.spring.booking.accommodationbookingservice.domain.Booking;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;

abstract class BaseStripeClient {
    static final String STRIPE_PAYMENT_CURRENCY = "usd";
    static final String STRIPE_PAYMENT_METHOD = "pm_card_visa";
    static final String STRIPE_PAYMENT_METHOD_TYPE = "card";
    static final String STRIPE_PAYMENT_SUCCESS_URL = "https://example.com/success";
    static final String STRIPE_PAYMENT_CANCEL_URL = "https://example.com/cancel";
    static final String STRIPE_PAYMENT_PRODUCT_DATA_NAME = "Booking #";
    static final String STRIPE_PAYMENT_SUCCESS_STATUS = "succeeded";
    static final String STRIPE_PAYMENT_CANCEL_STATUS_REGEX = "requires_";

    static final String STRIPE_SESSION_PAYMENT_SUCCESS_STATUS = "paid";

    static final Long STRIPE_PRODUCT_QUANTITY = 1L;

    protected Session stripeSession;
    protected PaymentIntent stripePayment;

    @Value("${stripe.key.secret}")
    private String stripeApiKey;

    @PostConstruct
    private void initSecretKey() {
        Stripe.apiKey = stripeApiKey;
    }

    abstract Session buildStripeSession(Booking booking, Accommodation accommodation)
            throws StripeException;

    abstract void buildStripeConfirmPayment() throws StripeException;

    abstract boolean isPaymentSuccess();

    abstract boolean isPaymentCanceled() throws StripeException;
}
