package com.github.ryanreymorris.orderescortbot.handler.command;

import com.github.ryanreymorris.orderescortbot.entity.Customer;
import com.github.ryanreymorris.orderescortbot.entity.Purchase;
import com.github.ryanreymorris.orderescortbot.handler.button.ButtonEnum;
import com.github.ryanreymorris.orderescortbot.handler.button.ButtonKeyboard;
import com.github.ryanreymorris.orderescortbot.service.BotMessageService;
import com.github.ryanreymorris.orderescortbot.service.CustomerService;
import com.github.ryanreymorris.orderescortbot.service.PurchaseService;
import com.github.ryanreymorris.orderescortbot.service.ReplyMessagesService;
import com.github.ryanreymorris.orderescortbot.storage.PaginationStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;

import java.text.MessageFormat;
import java.util.List;

/**
 * Buy {@link Command}.
 */
@Component
public class BuyCommand implements Command {

    private static final String BUY_MESSAGE = """
            Если вы желаете оформить заказ, то напишите мне артикул товара.
            Я оставлю заявку на ваш заказ, для уточнения всех деталей с вами в скором времени свяжется оператор.
            Вы можете вызывать команду /buy множества раз, все заявки будут переданы оператору.
            """;

    private static final String NO_PURCHASE_REQUEST = """
            На текущий момент нет активных заказов.
            Отдыхайте!
            """;

    private static final String SERVICE_CALL_REQUESTS_EXIST = "У вас имеются необработанные заказы:";

    private static final String HAS_NO_CONTACT = """
            :exclamation: К сожалению, мы не знаем ваши контакты.
            Пожалуйста, предоставьте нам свой контакт, который в последующем будет перед оператору.
            Без этого мы не сможем связаться с вами :pensive:
            """;

    @Autowired
    private ReplyMessagesService replyMessagesService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private BotMessageService botMessageService;

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private PaginationStorage storage;

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleCommand(Update update) {
        Message message = update.hasCallbackQuery() ? update.getCallbackQuery().getMessage() : update.getMessage();
        Customer customer = customerService.findById(message.getChatId());
        if (customer.isServiceCall()) {
            storage.resetPagination(customer.getId());
            Long userId = customer.getId();
            List<Purchase> purchaseList = purchaseService.findAllByPerformerId(userId);
            List<Customer> authors = purchaseList.stream()
                    .map(Purchase::getAuthor)
                    .distinct()
                    .toList();
            if (authors.size() == 0) {
                SendMessage sendMessage = replyMessagesService.createMessage(NO_PURCHASE_REQUEST, userId);
                botMessageService.updateLastMessage(sendMessage, update);
                return;
            }
            ButtonKeyboard buttonKeyboard = getPaginatedButtons(userId, authors);
            SendMessage sendMessage = replyMessagesService.createMessageWithButtons(SERVICE_CALL_REQUESTS_EXIST, customer.getId(), buttonKeyboard.getMessageButtons());
            botMessageService.updateLastMessage(sendMessage, update);
            return;
        }
        if (customer.getContactInfo() == null) {
            ButtonKeyboard buttonKeyboard = new ButtonKeyboard();
            KeyboardButton keyboardButton = new KeyboardButton();
            keyboardButton.setText("Предоставить контакты");
            keyboardButton.setRequestContact(true);
            buttonKeyboard.addReplyButton(0, keyboardButton);
            SendMessage sendMessage = replyMessagesService.createMessageWithMenuButtons(HAS_NO_CONTACT, customer.getId(), buttonKeyboard.getReplyButtons());
            botMessageService.sendNewMessageToUser(sendMessage, update);
            return;
        }
        customer.setCurrentCommand(BotCommandEnum.BUY);
        customerService.save(customer);
        SendMessage sendMessage = replyMessagesService.createMessage(BUY_MESSAGE, customer.getId());
        botMessageService.updateLastMessage(sendMessage, update);
    }

    /**
     * Get paginated buttons of service call requests.
     *
     * @param userId  - id of user
     * @param authors - list of purchases authors.
     * @return buttons.
     */
    private ButtonKeyboard getPaginatedButtons(Long userId, List<Customer> authors) {
        //Get "from" and "to" pagination value
        Integer paginationCount = storage.getValue(userId);
        if (paginationCount == null) {
            paginationCount = storage.increaseValue(userId);
        }
        int from = paginationCount - storage.getDeltaValue();
        int to = Math.min(paginationCount, authors.size());
        //create buttons
        ButtonKeyboard buttonKeyboard = new ButtonKeyboard();
        for (int i = from; i < to; i++) {
            Customer author = authors.get(i);
            String buttonData = MessageFormat.format("{0}{1}", ButtonEnum.PURCHASE_AUTHOR.getCode(), author.getId().toString());
            buttonKeyboard.addMessageButton(i, buttonData, author.getUserName());
        }
        if (from != 0) {
            buttonKeyboard.addMessageButton(to, ButtonEnum.UNDO.getCode(), ButtonEnum.UNDO.getName());
        }
        if (paginationCount < authors.size()) {
            buttonKeyboard.addMessageButton(to, ButtonEnum.REDO.getCode(), ButtonEnum.REDO.getName());
        }
        buttonKeyboard.addMessageButton(to + 1, ButtonEnum.EXIT.getCode(), ButtonEnum.EXIT.getName());
        return buttonKeyboard;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BotCommandEnum getBotcommand() {
        return BotCommandEnum.BUY;
    }
}