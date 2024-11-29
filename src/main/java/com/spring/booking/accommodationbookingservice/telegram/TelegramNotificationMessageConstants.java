package com.spring.booking.accommodationbookingservice.telegram;

public final class TelegramNotificationMessageConstants {
    static final String TELEGRAM_CREATE_ACCOMMODATION_NOTIFICATION_MESSAGE = """
            ✅Available accommodation:
            🆔ID: %d
            🛌Type: %s
            🌎Location: country=%s, city=%s, street=%s, postalCode=%s
            🏠Size: %s
            📦Amenities: %s
            💶DailyRate: %s
            🟢Availability: %d""";

    static final String TELEGRAM_CREATE_BOOKING_NOTIFICATION_MESSAGE = """
            🏪Your booking:
            🗓Check-In-Date: %s
            🗓Check-Out-Date: %s
            🆔Accommodation ID: %s
            🌐User ID: %s
            ✅Status: %s
            """;

    static final String TELEGRAM_CANCEL_BOOKING_NOTIFICATION_MESSAGE = """
            ❌ Cancelled booking:
            🗓Check-In-Date: %s
            🗓Check-Out-Date: %s
            🆔Accommodation ID: %s
            🌐User ID: %s
            ✅Status: %s
            """;

    static final String TELEGRAM_CREATE_PAYMENT_NOTIFICATION_MESSAGE = """
            💰Accepted payment:
            🌐Url: %s
            🆔ID: %s
            ✅Status: %s
            💲Amount to pay: %s
            """;

    private TelegramNotificationMessageConstants() {
    }
}
