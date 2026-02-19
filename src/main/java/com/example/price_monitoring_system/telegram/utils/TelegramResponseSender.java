package com.example.price_monitoring_system.telegram.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
@RequiredArgsConstructor
public class TelegramResponseSender {
    private final TelegramClient telegramClient;

    public void sendMessage(SendMessage response) {
        try {
            telegramClient.execute(response);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
