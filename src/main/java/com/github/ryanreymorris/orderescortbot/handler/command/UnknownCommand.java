package com.github.ryanreymorris.orderescortbot.handler.command;

import com.github.ryanreymorris.orderescortbot.entity.Customer;
import com.github.ryanreymorris.orderescortbot.service.BotMessageService;
import com.github.ryanreymorris.orderescortbot.service.CustomerService;
import com.github.ryanreymorris.orderescortbot.service.ReplyMessagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Unknown {@link Command}.
 */
@Component
public class UnknownCommand  implements Command {

    private final static String UNKNOWN_MESSAGE = """
                К сожалению, я не знаю такой команды :no_mouth:
                Напишите мне /help, чтобы узнать о доступных командах.
                """;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ReplyMessagesService replyMessagesService;

    @Autowired
    private BotMessageService botMessageService;

    @Override
    public void handleCommand(Update update) {
        Customer customer = customerService.findCustomerById(update.getMessage().getFrom().getId());
        SendMessage sendMessage = replyMessagesService.createMessage(UNKNOWN_MESSAGE, customer.getId());
        botMessageService.updateLastMessage(sendMessage, update);
    }

    @Override
    public BotCommandEnum getBotcommand() {
        return BotCommandEnum.UNKNOWN;
    }
}
