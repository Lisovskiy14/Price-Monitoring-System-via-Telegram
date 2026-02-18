package com.example.price_monitoring_system.telegram.messageResolver.impl;

import com.example.price_monitoring_system.domain.*;
import com.example.price_monitoring_system.telegram.messageResolver.ResponseMessageResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ResponseMessageResolverImpl implements ResponseMessageResolver {

    private final NumberFormat uaFormat;

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
    public SendMessage fromRegister(TrackedItem trackedItem, Long chatId) {
        String responseText = buildResponseTextOnRegister(trackedItem);
        return SendMessage.builder()
                .chatId(chatId)
                .text(responseText)
                .build();
    }

    @Override
    public SendMessage fromStartCommand(Message message) {
        String responseText = """
                Привіт, я Слон Наглядач!
                Я доглядаю за твоїми бажаними товарами, просто дай мені посилання.
                """;
        return SendMessage.builder()
                .chatId(message.getChatId())
                .text(responseText)
                .build();
    }

    @Override
    public SendMessage fromDefault(Message message) {
        String responseText = """
                Я вас не розумію.
                """;
        return SendMessage.builder()
                .chatId(message.getChatId())
                .text(responseText)
                .build();
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
                    "Попередня ціна - %s грн. Нова ціна - %s грн.",
                    uaFormat.format(previousPrice), uaFormat.format(currentPrice)));
        } else {
            sb.append("Товар знову в наявності!");
        }

        sb.append("\n\n");
        sb.append(buildItemInfo(trackedItem));

        return sb.toString();
    }

    private String buildResponseTextOnRegister(TrackedItem trackedItem) {
        StringBuilder sb = new StringBuilder();

        sb.append("Ваш новий товар:\n\n");
        sb.append(buildItemInfo(trackedItem));

        return sb.toString();
    }

    private StringBuilder buildItemInfo(TrackedItem trackedItem) {
        StringBuilder sb = new StringBuilder();

        Product product = trackedItem.getProduct();
        sb.append(product.getName());
        sb.append("\n");
        sb.append("Ціна: ").append(uaFormat.format(product.getPrice()));
        sb.append("\n");
        sb.append("Наявність: ").append(product.isAvailable());

        sb.append("\n\n");

        Shop shop = trackedItem.getShop();
        sb.append("Магазин: ").append(shop.getDomain());
        sb.append("\n");
        sb.append("Посилання: ").append(trackedItem.getUrl());

        return sb;
    }
}
