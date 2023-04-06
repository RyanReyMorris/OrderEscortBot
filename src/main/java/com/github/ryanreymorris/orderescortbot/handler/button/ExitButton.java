package com.github.ryanreymorris.orderescortbot.handler.button;

import com.github.ryanreymorris.orderescortbot.entity.Customer;
import com.github.ryanreymorris.orderescortbot.service.BotMessageService;
import com.github.ryanreymorris.orderescortbot.service.CustomerService;
import com.github.ryanreymorris.orderescortbot.service.ReplyMessagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Exit {@link Button}.
 */
@Component
public class ExitButton implements Button {

    private final static String EXIT_MESSAGE = """
            Обращайтесь, буду рад вам помочь!
            """;

    @Autowired
    private ReplyMessagesService replyMessagesService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private BotMessageService botMessageService;

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleClick(Update update) {
        Customer customer = customerService.findById(update.getCallbackQuery().getFrom().getId());
        SendMessage sendMessage = replyMessagesService.createMessage(EXIT_MESSAGE, customer.getId());
        botMessageService.updateLastMessage(sendMessage, update);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ButtonEnum getButton() {
        return ButtonEnum.EXIT;
    }
}
