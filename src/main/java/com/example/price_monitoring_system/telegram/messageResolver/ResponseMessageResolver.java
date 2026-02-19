package com.example.price_monitoring_system.telegram.messageResolver;

import com.example.price_monitoring_system.domain.ItemSnapshot;
import com.example.price_monitoring_system.domain.TrackedItem;
import com.example.price_monitoring_system.dto.RegistrationResult;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.List;

public interface ResponseMessageResolver {
    List<SendMessage> fromNotify(ItemSnapshot itemSnapshot);
    List<SendMessage> fromStartCommand(Message message);
    List<SendMessage> fromAllCommand(List<TrackedItem> trackedItems, Long chatId);
    List<SendMessage> fromRemoveCommand(List<String> urls, Long chatId);
    List<SendMessage> fromDefault(Message message);
    SendMessage fromRegister(RegistrationResult registrationResult, Long chatId);
    SendMessage fromUnexpectedError(Long chatId);
}
