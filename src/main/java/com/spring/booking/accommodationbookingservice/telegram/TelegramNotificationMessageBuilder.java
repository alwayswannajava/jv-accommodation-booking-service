package com.spring.booking.accommodationbookingservice.telegram;

import com.spring.booking.accommodationbookingservice.domain.Booking;
import com.spring.booking.accommodationbookingservice.dto.accommodation.AccommodationResponse;
import com.spring.booking.accommodationbookingservice.dto.booking.BookingResponse;

public interface TelegramNotificationMessageBuilder {
    String buildNotificationMessage(AccommodationResponse response);

    String buildNotificationMessage(BookingResponse response);

    String buildNotificationMessage(Booking booking);
}

