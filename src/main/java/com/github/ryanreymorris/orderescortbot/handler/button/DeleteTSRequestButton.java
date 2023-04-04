package com.github.ryanreymorris.orderescortbot.handler.button;

import com.github.ryanreymorris.orderescortbot.service.BotMessageService;
import com.github.ryanreymorris.orderescortbot.service.ReplyMessagesService;
import com.github.ryanreymorris.orderescortbot.service.ServiceCallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.github.ryanreymorris.orderescortbot.handler.button.ButtonEnum.DELETE_TS_REQUEST;

/**
 * Delete ts request {@link Button}.
 */
@Component
public class DeleteTSRequestButton implements Button {

    private static final String SUCCESS_DELETE_MESSAGE = "Заявка была успешно удалена!";

    @Autowired
    private BotMessageService botMessageService;

    @Autowired
    private ServiceCallService serviceCallService;

    @Autowired
    private ReplyMessagesService replyMessagesService;

    @Override
    public void handleClick(Update update) {
        Long userId = update.getCallbackQuery().getMessage().getChatId();
        Long authorId = Long.parseLong(update.getCallbackQuery().getData().replace(DELETE_TS_REQUEST.getCode(), ""));
        serviceCallService.delete(authorId);
        SendMessage sendMessage = replyMessagesService.createMessage(SUCCESS_DELETE_MESSAGE, userId);
        botMessageService.updateLastMessage(sendMessage, update);
    }

    @Override
    public ButtonEnum getButton() {
        return ButtonEnum.DELETE_TS_REQUEST;
    }
}
