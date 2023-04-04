package com.github.ryanreymorris.orderescortbot.handler.command;

import com.github.ryanreymorris.orderescortbot.entity.Customer;
import com.github.ryanreymorris.orderescortbot.service.BotMessageService;
import com.github.ryanreymorris.orderescortbot.service.CustomerService;
import com.github.ryanreymorris.orderescortbot.service.ReplyMessagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class DeliveryCommand implements Command {

    private final static String DELIVERY_MESSAGE = """
            :airplane: Все товары доставляются в РФ из Москвы.
            :car: В пределах города вы можете оформить самовывоз или же заказать доставку через Яндекс Такси.
            :package: В остальных случаях доставка осуществляется первым классом Почтой России.
            :stopwatch: Ориентировочный срок доставки - неделя.
            :gear: Для расчета стоимости доставки введите почтовый индекс вашего отделения.
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
        customer.setCurrentCommand(getBotcommand());
        customerService.saveCustomer(customer);
        SendMessage sendMessage = replyMessagesService.createMessage(DELIVERY_MESSAGE, customer.getId());
        botMessageService.updateLastMessage(sendMessage, update);
    }

    @Override
    public BotCommandEnum getBotcommand() {
        return BotCommandEnum.DELIVERY;
    }
}
