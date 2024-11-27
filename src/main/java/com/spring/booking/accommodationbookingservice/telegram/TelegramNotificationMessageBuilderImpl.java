package com.spring.booking.accommodationbookingservice.telegram;

import static com.spring.booking.accommodationbookingservice.telegram.TelegramNotificationMessageConstants.TELEGRAM_CANCEL_BOOKING_NOTIFICATION_MESSAGE;
import static com.spring.booking.accommodationbookingservice.telegram.TelegramNotificationMessageConstants.TELEGRAM_CREATE_ACCOMMODATION_NOTIFICATION_MESSAGE;
import static com.spring.booking.accommodationbookingservice.telegram.TelegramNotificationMessageConstants.TELEGRAM_CREATE_BOOKING_NOTIFICATION_MESSAGE;

import com.spring.booking.accommodationbookingservice.domain.Booking;
import com.spring.booking.accommodationbookingservice.dto.accommodation.AccommodationResponse;
import com.spring.booking.accommodationbookingservice.dto.booking.BookingResponse;
import org.springframework.stereotype.Service;

@Service
public class TelegramNotificationMessageBuilderImpl implements TelegramNotificationMessageBuilder {
    @Override
    public String buildNotificationMessage(AccommodationResponse response) {
        return String.format(TELEGRAM_CREATE_ACCOMMODATION_NOTIFICATION_MESSAGE,
                response.id(), response.type(), response.address().getCountry(),
                response.address().getCity(), response.address().getStreet(),
                response.address().getPostalCode(), response.size(), response.amenities(),
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

}
