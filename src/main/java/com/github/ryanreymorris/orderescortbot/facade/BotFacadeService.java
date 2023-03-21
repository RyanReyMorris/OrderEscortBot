package com.github.ryanreymorris.orderescortbot.facade;

import com.github.ryanreymorris.orderescortbot.entity.Customer;
import com.github.ryanreymorris.orderescortbot.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class BotFacadeService {

    @Autowired
    private CustomerService customerService;

    public BotApiMethod<?> handleUpdate(Update update) {
        checkIsNewCustomer(update.getMessage());
        Message message = update.getMessage() == null ? update.getCallbackQuery().getMessage() : update.getMessage();
        Customer customer = customerService.findCustomerById(message.getChatId());
//        return systemHandlers.get(updateType).handle(update);
        return null;
    }

    private void checkIsNewCustomer(Message message) {
        boolean isNewCustomer = message != null && customerService.checkIfCustomerIsNew(message.getChat().getId());
        if (isNewCustomer) {
            customerService.createNewCustomer(message);
        }
    }
}
