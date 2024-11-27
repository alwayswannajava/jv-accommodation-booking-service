package com.spring.booking.accommodationbookingservice.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramNotificationServiceImpl implements TelegramNotificationService {
    private final TelegramBot telegramBot;

    @Override
    public void sendMessage(String message) {
        telegramBot.sendMessage(message);
    }
}
