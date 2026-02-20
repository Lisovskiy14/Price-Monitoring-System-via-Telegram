package com.example.price_monitoring_system.telegram.config;

import dev.akkinoc.util.YamlResourceBundle;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;

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

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource() {
            @Override
            protected ResourceBundle doGetBundle(String basename, Locale locale) {
                return ResourceBundle.getBundle(basename, locale, YamlResourceBundle.Control.INSTANCE);
            }
        };

        messageSource.setBasename("messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setFallbackToSystemLocale(false);

        return messageSource;
    }

    @Bean
    public Locale locale() {
        return Locale.of("uk", "UA");
    }
}
