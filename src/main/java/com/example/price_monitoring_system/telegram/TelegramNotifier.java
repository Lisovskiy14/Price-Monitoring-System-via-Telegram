package com.example.price_monitoring_system.telegram;

import com.example.price_monitoring_system.domain.ItemSnapshot;
import com.example.price_monitoring_system.telegram.messageResolver.ResponseMessageResolver;
import com.example.price_monitoring_system.telegram.utils.TelegramResponseSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramNotifier {

    private final TelegramResponseSender sender;
    private final ResponseMessageResolver responseMessageResolver;

    public void notifyAll(List<ItemSnapshot> itemSnapshots) {
        log.info("Sending notifications...");

        List<SendMessage> responses = itemSnapshots.stream()
                .flatMap(itemSnapshot -> responseMessageResolver.fromNotify(itemSnapshot)
                        .stream())
                .toList();

        responses.forEach(response -> {
            try {
                sender.sendMessage(response);
                log.info("Notification sent to listener '{}'", response.getChatId());
            } catch (Exception ex) {
                log.warn("Failed to send notification to listener '{}'", response.getChatId());
                log.error(ex.getMessage());
            }
        });
        log.info("Sending notifications has been finished.");
    }
}
