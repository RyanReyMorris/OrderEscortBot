package com.github.ryanreymorris.orderescortbot.handler.button;

import com.github.ryanreymorris.orderescortbot.entity.Customer;
import com.github.ryanreymorris.orderescortbot.handler.command.OtherCommand;
import com.github.ryanreymorris.orderescortbot.service.CustomerService;
import com.github.ryanreymorris.orderescortbot.storage.PaginationStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Redo {@link Button}.
 */
@Component
public class RedoButton implements Button{

    @Autowired
    private PaginationStorage storage;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OtherCommand command;

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleClick(Update update) {
        Customer user = customerService.findById(update.getCallbackQuery().getMessage().getChatId());
        storage.increaseValue(user.getId());
        command.handleCommand(update);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ButtonEnum getButton() {
        return ButtonEnum.REDO;
    }
}
