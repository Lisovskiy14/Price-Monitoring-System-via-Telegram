package com.example.price_monitoring_system.telegram.config;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.text.NumberFormat;
import java.util.Locale;

@Configuration
public class TelegramConfig {

    @Bean
    public TelegramClient telegramClient(@Value("${telegram.bot.token}") String botToken) {
        return new OkHttpTelegramClient(botToken);
    }

    @Bean
    public UrlValidator urlValidator() {
        String[] schemes = {"https"};
        return new UrlValidator(schemes);
    }

    @Bean
    public NumberFormat uaFormat() {
        return NumberFormat.getCurrencyInstance(
                Locale.of("uk", "UA"));
    }
}
