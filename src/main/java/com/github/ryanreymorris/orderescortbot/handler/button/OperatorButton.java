package com.github.ryanreymorris.orderescortbot.handler.button;

import com.github.ryanreymorris.orderescortbot.entity.Customer;
import com.github.ryanreymorris.orderescortbot.entity.ServiceCall;
import com.github.ryanreymorris.orderescortbot.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;

import java.text.MessageFormat;
import java.util.Optional;

/**
 * Operator {@link Button}.
 */
@Component
public class OperatorButton implements Button {

    private static final String SERVICE_CALL_MESSAGE = """
            :exclamation: Ваша заявка была принята в работу!
            :black_small_square: В ближайшее время с вами свяжется оператор!
            """;

    private static final String NO_TEC_SUPPORT = """
            :exclamation: К сожалению, в данный момент нет ни одного сводобного оператора.
            Попробуйте выполнить запрос позже.
            """;

    private static final String ALREADY_HAS_SC_REQUEST = """
            :exclamation: Вы уже отправили заявку оператору.
            Пожалуйста, дождитесь ответа.
            """;

    private static final String HAS_NO_CONTACT = """
            :exclamation: К сожалению, мы не знаем ваши контакты.
            Пожалуйста, предоставьте нам свой контакт, который в последующем будет перед оператору.
            Без этого мы не сможем связаться с вами :pensive:
            """;

    private static final String TS_REQUEST = "Вам поступила новая заявка на техподдержку от: {0}";

    @Autowired
    private ReplyMessagesService replyMessagesService;

    @Autowired
    private ServiceCallService serviceCallService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private BotMessageService botMessageService;

    @Override
    public void handleClick(Update update) {
        Customer author = customerService.findCustomerById(update.getCallbackQuery().getFrom().getId());
        if (author.getContactInfo() == null) {
            ButtonKeyboard buttonKeyboard = new ButtonKeyboard();
            KeyboardButton keyboardButton  = new KeyboardButton();
            keyboardButton.setText("Предоставить контакты");
            keyboardButton.setRequestContact(true);
            buttonKeyboard.addReplyButton(0, keyboardButton);
            SendMessage sendMessage = replyMessagesService.createMessageWithMenuButtons(HAS_NO_CONTACT, author.getId(), buttonKeyboard.getReplyButtons());
            botMessageService.sendNewMessageToUser(sendMessage, update);
            return;
        }
        if (author.isInTecSupProcess()) {
            SendMessage sendMessage = replyMessagesService.createMessage(ALREADY_HAS_SC_REQUEST, author.getId());
            botMessageService.updateLastMessage(sendMessage, update);
            return;
        }
        Optional<Customer> performer = customerService.findLessBusyTecSupport();
        if (performer.isEmpty()) {
            SendMessage sendMessage = replyMessagesService.createMessage(NO_TEC_SUPPORT, author.getId());
            botMessageService.updateLastMessage(sendMessage, update);
            return;
        }
        author.setInTecSupProcess(true);
        customerService.saveCustomer(author);
        ServiceCall serviceCall = new ServiceCall();
        serviceCall.setId(author.getId());
        serviceCall.setAuthor(author);
        serviceCall.setPerformer(performer.get());
        serviceCallService.save(serviceCall);
        SendMessage sendMessageForAuthor = replyMessagesService.createMessage(SERVICE_CALL_MESSAGE, author.getId());
        botMessageService.updateLastMessage(sendMessageForAuthor, update);
        SendMessage sendMessageForPerformer = replyMessagesService.createMessage(MessageFormat.format(TS_REQUEST, author.getUserName()), performer.get().getId());
        botMessageService.sendNewMessageToUser(sendMessageForPerformer);
    }

    @Override
    public ButtonEnum getButton() {
        return ButtonEnum.OPERATOR;
    }
}
