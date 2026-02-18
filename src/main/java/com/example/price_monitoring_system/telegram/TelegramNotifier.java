package com.example.price_monitoring_system.telegram;

import com.example.price_monitoring_system.domain.ItemSnapshot;
import com.example.price_monitoring_system.telegram.messageResolver.ResponseMessageResolver;
import com.example.price_monitoring_system.telegram.utils.TelegramResponseSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TelegramNotifier {

    private final TelegramResponseSender sender;
    private final ResponseMessageResolver responseMessageResolver;

    public void notifyAll(List<ItemSnapshot> itemSnapshots) {
        List<SendMessage> responses = itemSnapshots.stream()
                .flatMap(itemSnapshot -> responseMessageResolver.fromNotify(itemSnapshot)
                        .stream())
                .toList();

        responses.forEach(sender::sendMessage);
    }
}
