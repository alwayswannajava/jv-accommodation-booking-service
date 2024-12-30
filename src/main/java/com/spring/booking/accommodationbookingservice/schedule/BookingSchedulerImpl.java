package com.spring.booking.accommodationbookingservice.schedule;

import static com.spring.booking.accommodationbookingservice.telegram.impl.TelegramNotificationMessageConstants.NO_EXPIRED_BOOKINGS_MESSAGE;

import com.spring.booking.accommodationbookingservice.domain.Booking;
import com.spring.booking.accommodationbookingservice.domain.enums.Status;
import com.spring.booking.accommodationbookingservice.repository.BookingRepository;
import com.spring.booking.accommodationbookingservice.telegram.NotificationMessageBuilder;
import com.spring.booking.accommodationbookingservice.telegram.TelegramNotificationService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookingSchedulerImpl implements BookingScheduler {
    private final BookingRepository bookingRepository;
    private final TelegramNotificationService telegramNotificationService;
    private final NotificationMessageBuilder notificationMessageBuilder;

    @Override
    @Scheduled(fixedRate = 500000)
    public void scheduleExpiredBookings() {
        List<Booking> bookings = getExpiredBookings();
        if (bookings.isEmpty()) {
            telegramNotificationService.sendMessage(NO_EXPIRED_BOOKINGS_MESSAGE);
        } else {
            bookings.forEach(booking -> {
                booking.setStatus(Status.EXPIRED);
                bookingRepository.save(booking);
                String builtNotificationMessage = notificationMessageBuilder
                        .buildNotificationMessage(booking);
                telegramNotificationService.sendMessage(builtNotificationMessage);
            });
        }
    }

    private List<Booking> getExpiredBookings() {
        return bookingRepository.findAll()
                .stream()
                .filter(booking -> !booking.isDeleted())
                .filter(booking -> booking.getCheckOutDate().isBefore(LocalDate.now()))
                .toList();
    }
}
