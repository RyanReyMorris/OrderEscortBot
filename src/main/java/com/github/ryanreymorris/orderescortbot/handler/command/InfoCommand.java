package com.github.ryanreymorris.orderescortbot.handler.command;

import com.github.ryanreymorris.orderescortbot.entity.Customer;
import com.github.ryanreymorris.orderescortbot.service.BotMessageService;
import com.github.ryanreymorris.orderescortbot.service.CustomerService;
import com.github.ryanreymorris.orderescortbot.service.ReplyMessagesService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.text.MessageFormat;

/**
 * Info {@link Command}.
 */
@Component
public class InfoCommand implements Command{

    private final static String INFO_MESSAGE = "Пришлите пожалуйста артикул интересующего вас товара!";

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
    public void handleCommand(Update update) {
        Customer customer = customerService.findCustomerById(update.getMessage().getFrom().getId());
        customer.setCurrentCommand(getBotcommand());
        customerService.saveCustomer(customer);
        SendMessage sendMessage = replyMessagesService.createMessage(INFO_MESSAGE, customer.getId());
        botMessageService.updateLastMessage(sendMessage, update);
    }

    @Override
    public BotCommandEnum getBotcommand() {
        return BotCommandEnum.INFO;
    }
}
