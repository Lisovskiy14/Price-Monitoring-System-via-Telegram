package com.example.price_monitoring_system.telegram.config;

import com.example.price_monitoring_system.telegram.UpdateConsumer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;

@Component
@RequiredArgsConstructor
public class TelegramBot implements SpringLongPollingBot {

    @Value("${telegram.bot.token}")
    private String token;

    private final UpdateConsumer updateConsumer;

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return updateConsumer;
    }
}
