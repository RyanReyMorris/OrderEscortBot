package com.github.ryanreymorris.orderescortbot.facade;

import com.github.ryanreymorris.orderescortbot.handler.UpdateHandler;
import com.github.ryanreymorris.orderescortbot.handler.UpdateType;
import com.github.ryanreymorris.orderescortbot.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

/**
 * Facade service of bot. It processes incoming update from user and sends it to proper handler.
 */
@Service
public class BotFacadeService {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private Map<UpdateType, UpdateHandler> updateHandlers;

    /**
     * Handle incoming update from customer. Dedicate update to proper handler
     *
     * @param update - incoming update
     */
    public void handleUpdate(Update update) {
        checkIsNewCustomer(update.getMessage());
        UpdateType updateType = getHandlerType(update);
        updateHandlers.get(updateType).handle(update);
    }

    /**
     * Check if incoming message is from new user. If user is new - create new customer in db
     *
     * @param message - message object
     */
    private void checkIsNewCustomer(Message message) {
        boolean isNewCustomer = message != null && customerService.checkIfCustomerIsNew(message.getChat().getId());
        if (isNewCustomer) {
            customerService.create(message);
        }
    }

    /**
     * Get type of user's update actions
     *
     * @param update - sent update
     * @return type of update
     */
    private UpdateType getHandlerType(Update update) {
        if (update.hasCallbackQuery()) {
            return UpdateType.BUTTON_CLICK;
        } else if (update.getMessage().hasEntities() && update.getMessage().getEntities().get(0).getType().equals("bot_command")) {
            return UpdateType.BOT_COMMAND;
        } else {
            return UpdateType.TEXT_MESSAGE;
        }
    }
}
