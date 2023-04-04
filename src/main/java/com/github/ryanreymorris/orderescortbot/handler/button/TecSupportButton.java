package com.github.ryanreymorris.orderescortbot.handler.button;

import com.github.ryanreymorris.orderescortbot.entity.Customer;
import com.github.ryanreymorris.orderescortbot.service.BotMessageService;
import com.github.ryanreymorris.orderescortbot.service.CustomerService;
import com.github.ryanreymorris.orderescortbot.service.ReplyMessagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.text.MessageFormat;

import static com.github.ryanreymorris.orderescortbot.handler.button.ButtonEnum.TEC_SUPPORT;

/**
 * TecSupport {@link Button}.
 */
@Component
public class TecSupportButton implements Button {

    private static final String GET_AUTHOR_CONTACT = "Выберите действие по заявке:";

    @Autowired
    private ReplyMessagesService replyMessagesService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private BotMessageService botMessageService;

    @Override
    public void handleClick(Update update) {
        Customer user = customerService.findCustomerById(update.getCallbackQuery().getMessage().getChatId());
        Long authorId = Long.parseLong(update.getCallbackQuery().getData().replace(TEC_SUPPORT.getCode(), ""));
        String getButtonData = MessageFormat.format("{0}{1}", ButtonEnum.GET_CONTACT.getCode(), authorId.toString());
        String deleteButtonData = MessageFormat.format("{0}{1}", ButtonEnum.DELETE_TS_REQUEST.getCode(), authorId.toString());
        ButtonKeyboard buttonKeyboard = new ButtonKeyboard();
        buttonKeyboard.addMessageButton(0, getButtonData, ButtonEnum.GET_CONTACT.getName());
        buttonKeyboard.addMessageButton(1, deleteButtonData, ButtonEnum.DELETE_TS_REQUEST.getName());
        buttonKeyboard.addMessageButton(2, ButtonEnum.EXIT.getCode(), ButtonEnum.EXIT.getName());
        SendMessage sendMessage = replyMessagesService.createMessageWithButtons(GET_AUTHOR_CONTACT, user.getId(), buttonKeyboard.getMessageButtons());
        botMessageService.updateLastMessage(sendMessage, update);
    }

    @Override
    public ButtonEnum getButton() {
        return TEC_SUPPORT;
    }
}
