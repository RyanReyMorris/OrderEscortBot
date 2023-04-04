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
 * Help {@link Command}.
 */
@Component
public class HelpCommand implements Command{

    private final static String HELP_MESSAGE = getHelpMessage();

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
        String messageText = MessageFormat.format(HELP_MESSAGE, customer.getName());
        SendMessage sendMessage = replyMessagesService.createMessage(messageText, customer.getId());
        botMessageService.updateLastMessage(sendMessage, update);
    }

    /**
     * Create start message.
     *
     * @return start message.
     */
    private static String getHelpMessage() {
        StringBuilder commandList = new StringBuilder();
        commandList.append("Ниже перечислены все доступные команды: \n");
        for (BotCommandEnum command : BotCommandEnum.values()) {
            if (StringUtils.isNotEmpty(command.getCommandDescription())) {
                commandList.append(MessageFormat.format(":gear: {0} - {1}; \n", command.getCommandName(), command.getCommandDescription()));
            }
        }
        commandList.append("Обращайтесь, буду рад вам помочь! \n ");
        return commandList.toString();
    }

    @Override
    public BotCommandEnum getBotcommand() {
        return BotCommandEnum.HELP;
    }
}
