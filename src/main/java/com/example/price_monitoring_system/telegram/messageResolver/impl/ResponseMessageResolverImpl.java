package com.example.price_monitoring_system.telegram.messageResolver.impl;

import com.example.price_monitoring_system.common.Availability;
import com.example.price_monitoring_system.domain.*;
import com.example.price_monitoring_system.dto.RegistrationResult;
import com.example.price_monitoring_system.telegram.messageResolver.ResponseMessageResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class ResponseMessageResolverImpl implements ResponseMessageResolver {

    private final NumberFormat uaFormat;
    private final MessageSource messageSource;
    private final Locale locale;

    @Override
    public List<SendMessage> fromNotify(ItemSnapshot itemSnapshot) {
        String responseText = buildResponseTextOnNotify(itemSnapshot);

        TrackedItem trackedItem = itemSnapshot.getTrackedItem();
        List<Long> chatIds = trackedItem.getListeners().stream()
                .map(User::getId)
                .toList();

        List<SendMessage> responses = new ArrayList<>();
        chatIds.forEach(chatId -> {
            SendMessage response = SendMessage.builder()
                    .chatId(chatId)
                    .text(responseText)
                    .build();

            responses.add(response);
        });

        return responses;
    }

    @Override
    public SendMessage fromRegister(RegistrationResult registrationResult, Long chatId) {
        String responseText = buildResponseTextOnRegister(registrationResult);
        return SendMessage.builder()
                .chatId(chatId)
                .text(responseText)
                .build();
    }

    @Override
    public List<SendMessage> fromStartCommand(Message message) {
        String responseText = """
                Привіт, я Слон Наглядач!
                Я доглядаю за твоїми бажаними товарами, просто дай мені посилання.
                """;
        return List.of(
                SendMessage.builder()
                        .chatId(message.getChatId())
                        .text(responseText)
                        .build()
        );
    }

    @Override
    public List<SendMessage> fromAllCommand(List<TrackedItem> trackedItems, Long chatId) {
        List<SendMessage> responses = new ArrayList<>();

        if (trackedItems.isEmpty()) {
            String responseText = """
                    Ви поки що не надали жодного товару.
                    """;
            SendMessage response = SendMessage.builder()
                    .chatId(chatId)
                    .text(responseText)
                    .build();

            responses.add(response);
            return responses;
        }

        String titleResponse = """
                Звісно! Ось ваші товари:
                """;
        responses.add(
                SendMessage.builder()
                        .chatId(chatId)
                        .text(titleResponse)
                        .build()
        );

        trackedItems.forEach(trackedItem -> {
            String itemInfo = buildItemInfo(trackedItem).toString();

            SendMessage response = SendMessage.builder()
                    .chatId(chatId)
                    .text(itemInfo)
                    .build();

            responses.add(response);
        });

        return responses;
    }

    @Override
    public List<SendMessage> fromDefault(Message message) {
        String responseText = """
                Я вас не розумію.
                """;
        return List.of(
                SendMessage.builder()
                        .chatId(message.getChatId())
                        .text(responseText)
                        .build()
        );
    }

    @Override
    public SendMessage fromUnexpectedError(Long chatId) {
        String responseText = """
                Сталась якась помилка. Схоже лупа зламалась...
                """;
        return SendMessage.builder()
                .chatId(chatId)
                .text(responseText)
                .build();
    }

    @Override
    public List<SendMessage> fromRemoveCommand(List<String> urls, Long chatId) {
        StringBuilder sb = new StringBuilder();

        sb.append("З вашого списку було видалено наступні товари:");
        sb.append("\n\n");

        int i = 1;
        for (String url : urls) {
            sb.append(i++).append(". ").append(url).append("\n");
        }

        return List.of(
                SendMessage.builder()
                        .chatId(chatId)
                        .text(sb.toString())
                        .build()
        );
    }

    private String buildResponseTextOnNotify(ItemSnapshot itemSnapshot) {
        StringBuilder sb = new StringBuilder();

        TrackedItem trackedItem = itemSnapshot.getTrackedItem();
        Product product = trackedItem.getProduct();

        BigDecimal currentPrice = product.getPrice();
        BigDecimal previousPrice = itemSnapshot.getPreviousPrice();
        if (currentPrice.compareTo(previousPrice) < 0) {
            sb.append("Ціна на товар знизилась!");
            sb.append("\n");
            sb.append(String.format(
                    "Попередня ціна - %s грн.\nНова ціна - %s грн.",
                    uaFormat.format(previousPrice), uaFormat.format(currentPrice)));
        } else {
            sb.append("Товар знову в наявності!");
        }

        sb.append("\n\n");
        sb.append(buildItemInfo(trackedItem));

        return sb.toString();
    }

    private String buildResponseTextOnRegister(RegistrationResult registrationResult) {
        StringBuilder sb = new StringBuilder();

        if (registrationResult.isAlreadyTracked()) {
            sb.append("Ви вже слідкуєте за цим товаром:");
        } else {
            sb.append("Ваш новий товар:");
        }

        sb.append("\n\n");
        sb.append(buildItemInfo(registrationResult.getTrackedItem()));

        return sb.toString();
    }

    private StringBuilder buildItemInfo(TrackedItem trackedItem) {
        StringBuilder sb = new StringBuilder();

        Product product = trackedItem.getProduct();
        sb.append(product.getName());
        sb.append("\n");
        sb.append("Ціна: ").append(uaFormat.format(product.getPrice()));
        sb.append("\n");

        Availability availability = product.getAvailability();
        String availabilityMessage = messageSource.getMessage(
                "availability." + availability.getValue(), null, locale);
        sb.append("Наявність: ").append(availabilityMessage);

        sb.append("\n\n");

        Shop shop = trackedItem.getShop();
        sb.append("Магазин: ").append(shop.getDomain());
        sb.append("\n");
        sb.append("Посилання: ").append(trackedItem.getUrl());

        return sb;
    }
}
