package com.github.ryanreymorris.orderescortbot.handler.button;

import com.github.ryanreymorris.orderescortbot.service.BotMessageService;
import com.github.ryanreymorris.orderescortbot.service.PurchaseService;
import com.github.ryanreymorris.orderescortbot.service.ReplyMessagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.github.ryanreymorris.orderescortbot.handler.button.ButtonEnum.DELETE_AUTHOR_PURCHASES;


/**
 * Delete author purchases {@link Button}.
 */
@Component
public class DeleteAuthorPurchasesButton implements Button {

    private static final String SUCCESS_DELETE_MESSAGE = "Заявки по заказам для данного автора были удалены!";

    @Autowired
    private BotMessageService botMessageService;

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private ReplyMessagesService replyMessagesService;

    @Override
    public void handleClick(Update update) {
        Long userId = update.getCallbackQuery().getMessage().getChatId();
        Long authorId = Long.parseLong(update.getCallbackQuery().getData().replace(DELETE_AUTHOR_PURCHASES.getCode(), ""));
        purchaseService.deleteAllByAuthorId(authorId);
        SendMessage sendMessage = replyMessagesService.createMessage(SUCCESS_DELETE_MESSAGE, userId);
        botMessageService.updateLastMessage(sendMessage, update);
    }

    @Override
    public ButtonEnum getButton() {
        return ButtonEnum.DELETE_AUTHOR_PURCHASES;
    }
}
