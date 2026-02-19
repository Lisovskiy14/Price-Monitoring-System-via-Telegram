package com.example.price_monitoring_system.telegram;

import com.example.price_monitoring_system.domain.TrackedItem;
import com.example.price_monitoring_system.dto.RegistrationResult;
import com.example.price_monitoring_system.service.TrackedItemService;
import com.example.price_monitoring_system.service.TrackingService;
import com.example.price_monitoring_system.telegram.messageResolver.ResponseMessageResolver;
import com.example.price_monitoring_system.telegram.utils.TelegramResponseSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.List;


@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateConsumer implements LongPollingSingleThreadUpdateConsumer {

    private final TelegramResponseSender sender;
    private final TrackingService trackingService;
    private final TrackedItemService trackedItemService;
    private final ResponseMessageResolver responseMessageResolver;
    private final UrlValidator urlValidator;

    @Override
    public void consume(Update update) {

        if (update == null || !update.hasMessage()) {
            return;
        }

        log.info("Received the message from client");
        Message message = update.getMessage();

        String url = message.getText().trim();
        if (urlValidator.isValid(url)) {
            processRegisterUrl(url, message.getChatId());
            return;
        }

        String[] messageComponents = message.getText().split(" ");
        String command = messageComponents[0];

        Long chatId = message.getChatId();
        List<SendMessage> responses = switch (command) {
            case "/start" -> responseMessageResolver.fromStartCommand(message);
            case "/all" -> {
                List<TrackedItem> trackedItems = trackedItemService.getTrackedItemsByListenerId(chatId);
                yield responseMessageResolver.fromAllCommand(trackedItems, chatId);
            }
            case "/remove" -> {
                List<String> urls = List.of(messageComponents).subList(1, messageComponents.length);

                if (urls.isEmpty()) {
                    yield responseMessageResolver.fromDefault(message);
                }

                urls = urls.stream()
                        .filter(urlValidator::isValid)
                        .toList();

                trackedItemService.removeListenerFromTrackedItems(urls, chatId);
                yield responseMessageResolver.fromRemoveCommand(urls, chatId);

            }
            default -> responseMessageResolver.fromDefault(message);
        };

        responses.forEach(sender::sendMessage);
    }

    private void processRegisterUrl(String url, Long chatId) {
        try {
            RegistrationResult registrationResult = trackingService.registerTrackedItem(url, chatId);
            sender.sendMessage(responseMessageResolver.fromRegister(registrationResult, chatId));
        } catch (RuntimeException ex) {
            log.error("Error during registering tracked item: {}", ex.getMessage());
            sender.sendMessage(responseMessageResolver.fromUnexpectedError(chatId));
        }

    }
}
