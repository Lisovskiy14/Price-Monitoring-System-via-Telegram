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
        StringBuilder sb = new StringBuilder();
        sb.append(messageSource.getMessage("welcome.title", null, locale));
        sb.append("\n");
        sb.append(messageSource.getMessage("welcome.description", null, locale));
        String responseText = sb.toString();
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
            String responseText = messageSource.getMessage("command.all.onEmpty.title", null, locale);
            SendMessage response = SendMessage.builder()
                    .chatId(chatId)
                    .text(responseText)
                    .build();

            responses.add(response);
            return responses;
        }

        String titleResponse = messageSource.getMessage("command.all.default.title", null, locale);
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
        String responseText = messageSource.getMessage("command.default.title", null, locale);
        return List.of(
                SendMessage.builder()
                        .chatId(message.getChatId())
                        .text(responseText)
                        .build()
        );
    }

    @Override
    public SendMessage fromUnexpectedError(Long chatId) {
        String responseText = messageSource.getMessage("error.unexpected.title", null, locale);
        return SendMessage.builder()
                .chatId(chatId)
                .text(responseText)
                .build();
    }

    @Override
    public List<SendMessage> fromRemoveCommand(List<String> urls, Long chatId) {
        StringBuilder sb = new StringBuilder();

        sb.append(messageSource.getMessage("command.remove.default.title", null, locale));
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
            sb.append(messageSource.getMessage("on-notify.become-cheaper.title", null, locale));
            sb.append("\n");
            String details = messageSource.getMessage(
                    "on-notify.become-cheaper.details",
                    new String[] {
                            uaFormat.format(previousPrice),
                            uaFormat.format(currentPrice)
                    },
                    locale
            );
            sb.append(details);
        } else {
            sb.append(messageSource.getMessage("on-notify.become-available.title", null, locale));
        }

        sb.append("\n\n");
        sb.append(buildItemInfo(trackedItem));

        return sb.toString();
    }

    private String buildResponseTextOnRegister(RegistrationResult registrationResult) {
        StringBuilder sb = new StringBuilder();

        if (registrationResult.isAlreadyTracked()) {
            sb.append(messageSource.getMessage("on-register.already-tracked.title", null, locale));
        } else {
            sb.append(messageSource.getMessage("on-register.default.title", null, locale));
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
        sb.append(messageSource.getMessage("tracked-item.product.price-field", null, locale))
                .append(uaFormat.format(product.getPrice()));
        sb.append("\n");

        Availability availability = product.getAvailability();
        String availabilityMessage = messageSource.getMessage(
                "tracked-item.product.availability." + availability.getValue(), null, locale);
        sb.append(availabilityMessage);

        sb.append("\n\n");

        Shop shop = trackedItem.getShop();
        sb.append(messageSource.getMessage("tracked-item.shop.domain-field", null, locale))
                .append(shop.getDomain());
        sb.append("\n");
        sb.append(messageSource.getMessage("tracked-item.shop.url-field", null, locale))
                .append(trackedItem.getUrl());

        return sb;
    }
}
