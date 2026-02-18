package com.example.price_monitoring_system.telegram.messageResolver;

import com.example.price_monitoring_system.domain.ItemSnapshot;
import com.example.price_monitoring_system.domain.TrackedItem;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.List;

public interface ResponseMessageResolver {
    List<SendMessage> fromNotify(ItemSnapshot itemSnapshot);
    SendMessage fromRegister(TrackedItem trackedItem, Long chatId);
    SendMessage fromStartCommand(Message message);
    SendMessage fromDefault(Message message);
    SendMessage fromUnexpectedError(Long chatId);
}
