package com.github.ryanreymorris.orderescortbot.handler.command;

import com.github.ryanreymorris.orderescortbot.entity.Customer;
import com.github.ryanreymorris.orderescortbot.entity.ServiceCall;
import com.github.ryanreymorris.orderescortbot.handler.button.ButtonEnum;
import com.github.ryanreymorris.orderescortbot.handler.button.ButtonKeyboard;
import com.github.ryanreymorris.orderescortbot.service.BotMessageService;
import com.github.ryanreymorris.orderescortbot.service.CustomerService;
import com.github.ryanreymorris.orderescortbot.service.ReplyMessagesService;
import com.github.ryanreymorris.orderescortbot.service.ServiceCallService;
import com.github.ryanreymorris.orderescortbot.storage.PaginationStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.text.MessageFormat;
import java.util.List;

/**
 * Other {@link Command}.
 */
@Component
public class OtherCommand implements Command {

    private final static String OTHER_MESSAGE = """
            :hammer_and_wrench: Если у вас остались вопросы, с которыми я не могу вам помочь, то вы можете обращиться в нашу техподдержку!
            :black_small_square: При нажатии на кнопку "Оператор" ваша заявка будет принята.
            :incoming_envelope: После этого вам в ближайшее время напишет первый свободный оператор.
            :back: Для выхода нажмите кнопку "Выйти".
            """;

    private final static String NO_TS_REQUEST = """
            На текущий момент нет активных заявок.
            Отдыхайте!
            """;

    private final static String SERVICE_CALL_REQUESTS_EXIST = "У вас имеются необработнные заявки:";

    @Autowired
    private ReplyMessagesService replyMessagesService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private BotMessageService botMessageService;

    @Autowired
    private ServiceCallService serviceCallService;

    @Autowired
    private PaginationStorage storage;

    @Override
    public void handleCommand(Update update) {
        Message message = update.hasCallbackQuery() ? update.getCallbackQuery().getMessage() : update.getMessage();
        Customer customer = customerService.findCustomerById(message.getChatId());
        if (customer.isServiceCall()) {
            storage.resetPagination(customer.getId());
            Long userId = customer.getId();
            List<ServiceCall> serviceCallList = serviceCallService.findAllByPerformerId(userId);
            if (serviceCallList.size() == 0) {
                SendMessage sendMessage = replyMessagesService.createMessage(NO_TS_REQUEST, userId);
                botMessageService.updateLastMessage(sendMessage, update);
                return;
            }
            ButtonKeyboard buttonKeyboard = getPaginatedButtons(userId, serviceCallList);
            SendMessage sendMessage = replyMessagesService.createMessageWithButtons(SERVICE_CALL_REQUESTS_EXIST, customer.getId(), buttonKeyboard.getMessageButtons());
            botMessageService.updateLastMessage(sendMessage, update);
            return;
        }
        ButtonKeyboard buttonKeyboard = new ButtonKeyboard();
        buttonKeyboard.addMessageButton(0, ButtonEnum.OPERATOR.getCode(), ButtonEnum.OPERATOR.getName());
        buttonKeyboard.addMessageButton(1, ButtonEnum.EXIT.getCode(), ButtonEnum.EXIT.getName());
        SendMessage sendMessage = replyMessagesService.createMessageWithButtons(OTHER_MESSAGE, customer.getId(), buttonKeyboard.getMessageButtons());
        botMessageService.updateLastMessage(sendMessage, update);
    }

    /**
     * Get paginated buttons of service call requests.
     *
     * @param userId          - id of user
     * @param serviceCallList - list of service call requests.
     * @return buttons.
     */
    private ButtonKeyboard getPaginatedButtons(Long userId, List<ServiceCall> serviceCallList) {
        //Get "from" and "to" pagination value
        Integer paginationCount = storage.getValue(userId);
        if (paginationCount == null) {
            paginationCount = storage.increaseValue(userId);
        }
        int from = paginationCount - storage.getDeltaValue();
        int to = Math.min(paginationCount, serviceCallList.size());
        //create buttons
        ButtonKeyboard buttonKeyboard = new ButtonKeyboard();
        for (int i = from; i < to; i++) {
            ServiceCall serviceCall = serviceCallList.get(i);
            String buttonData = MessageFormat.format("{0}{1}", ButtonEnum.TEC_SUPPORT.getCode(), serviceCall.getAuthor().getId().toString());
            buttonKeyboard.addMessageButton(i, buttonData, serviceCall.getAuthor().getUserName());
        }
        if (from != 0) {
            buttonKeyboard.addMessageButton(to, ButtonEnum.UNDO.getCode(), ButtonEnum.UNDO.getName());
        }
        if (paginationCount < serviceCallList.size()) {
            buttonKeyboard.addMessageButton(to, ButtonEnum.REDO.getCode(), ButtonEnum.REDO.getName());
        }
        buttonKeyboard.addMessageButton(to + 1, ButtonEnum.EXIT.getCode(), ButtonEnum.EXIT.getName());
        return buttonKeyboard;
    }

    @Override
    public BotCommandEnum getBotcommand() {
        return BotCommandEnum.OTHER;
    }
}
