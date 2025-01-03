package com.spring.booking.accommodationbookingservice.telegram.impl;

import static com.spring.booking.accommodationbookingservice.telegram.impl.TelegramNotificationMessageConstants.TELEGRAM_CANCEL_BOOKING_NOTIFICATION_MESSAGE;
import static com.spring.booking.accommodationbookingservice.telegram.impl.TelegramNotificationMessageConstants.TELEGRAM_CREATE_ACCOMMODATION_NOTIFICATION_MESSAGE;
import static com.spring.booking.accommodationbookingservice.telegram.impl.TelegramNotificationMessageConstants.TELEGRAM_CREATE_BOOKING_NOTIFICATION_MESSAGE;
import static com.spring.booking.accommodationbookingservice.telegram.impl.TelegramNotificationMessageConstants.TELEGRAM_CREATE_PAYMENT_NOTIFICATION_MESSAGE;

import com.spring.booking.accommodationbookingservice.domain.Booking;
import com.spring.booking.accommodationbookingservice.dto.accommodation.AccommodationResponse;
import com.spring.booking.accommodationbookingservice.dto.booking.BookingResponse;
import com.spring.booking.accommodationbookingservice.dto.payment.PaymentConfirmResponse;
import com.spring.booking.accommodationbookingservice.telegram.NotificationMessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class NotificationMessageBuilderImpl implements NotificationMessageBuilder {
    @Override
    public String buildNotificationMessage(AccommodationResponse response) {
        return String.format(TELEGRAM_CREATE_ACCOMMODATION_NOTIFICATION_MESSAGE,
                response.id(), response.type(), response.address().getCountry(),
                response.address().getCity(), response.address().getStreet(),
                response.address().getPostalCode(), response.size(),
                response.dailyRate(), response.availability());
    }

    @Override
    public String buildNotificationMessage(BookingResponse response) {
        return String.format(TELEGRAM_CREATE_BOOKING_NOTIFICATION_MESSAGE,
                response.checkInDate(),
                response.checkOutDate(),
                response.accommodationId(),
                response.userId(),
                response.status());
    }

    @Override
    public String buildNotificationMessage(Booking booking) {
        return String.format(TELEGRAM_CANCEL_BOOKING_NOTIFICATION_MESSAGE,
                booking.getCheckInDate(),
                booking.getCheckOutDate(),
                booking.getAccommodationId(),
                booking.getUserId(),
                booking.getStatus());
    }

    public String buildNotificationMessage(PaymentConfirmResponse response) {
        return String.format(TELEGRAM_CREATE_PAYMENT_NOTIFICATION_MESSAGE,
                response.message(),
                response.bookingId(),
                response.status(),
                response.amount());
    }
}
