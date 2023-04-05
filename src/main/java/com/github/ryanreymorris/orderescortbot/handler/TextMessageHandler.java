package com.github.ryanreymorris.orderescortbot.handler;

import com.github.ryanreymorris.orderescortbot.entity.Customer;
import com.github.ryanreymorris.orderescortbot.entity.Product;
import com.github.ryanreymorris.orderescortbot.entity.mail.RussianMailItem;
import com.github.ryanreymorris.orderescortbot.handler.button.ButtonEnum;
import com.github.ryanreymorris.orderescortbot.handler.button.ButtonKeyboard;
import com.github.ryanreymorris.orderescortbot.handler.command.BotCommandEnum;
import com.github.ryanreymorris.orderescortbot.mapper.ContactMapper;
import com.github.ryanreymorris.orderescortbot.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.text.MessageFormat;

/**
 * Handler of text message action.
 */
@Component
public class TextMessageHandler implements UpdateHandler {

    private static final String GET_CONTACT = """
            Большое спасибо за предоставленный контакт!
            """;

    private static final String UNKNOWN_VENDOR_CODE = """
            К сожалению, товара с таким артикулом не существует.
            Проверьте правильность введенных данных.
            """;

    private static final String MAIL_COST_MESSAGE = """
            Доставка до данного отделения будет составлять {0} рублей!
            """;

    private static final String UNKNOWN_MAIL_ADDRESS = """
            К сожалению, мне не удается найти почтовое отделение с указанным индексом, попробуйте еще раз!
            """;

    @Autowired
    private ReplyMessagesService replyMessagesService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private BotMessageService botMessageService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private ContactMapper mapper;

    @Autowired
    private ProductService productService;

    @Autowired
    private RussianMailService mailService;

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(Update update) {
        Customer customer = customerService.findById(update.getMessage().getChatId());
        Contact contact = update.getMessage().getContact();
        if (contact != null && contact.getUserId().equals(customer.getId())) {
            contactService.save(contact);
            customer.setContactInfo(mapper.contactToContactInfo(contact));
            customerService.save(customer);
            SendMessage sendMessage = replyMessagesService.createMessage(GET_CONTACT, customer.getId());
            botMessageService.updateLastMessage(sendMessage, update);
        } else {
            switch (customer.getCurrentCommand()) {
                case INFO:
                    Long vendorCode = Long.valueOf(update.getMessage().getText());
                    customer.setCurrentCommand(BotCommandEnum.UNKNOWN);
                    customerService.save(customer);
                    try {
                        Product product = productService.findById(vendorCode);
                        String caption = productService.createCaption(product);
                        SendPhoto sendPhoto = replyMessagesService.createMessageWithPhoto(product.getPhoto(), caption, customer.getId());
                        botMessageService.updateLastMessage(sendPhoto, update);
                    } catch (RuntimeException exception) {
                        SendMessage sendMessage = replyMessagesService.createMessage(UNKNOWN_VENDOR_CODE, customer.getId());
                        botMessageService.updateLastMessage(sendMessage, update);
                    }
                    break;
                case DELIVERY:
                    customer.setCurrentCommand(BotCommandEnum.UNKNOWN);
                    customerService.save(customer);
                    ResponseEntity<RussianMailItem> response;
                    try {
                        response = mailService.getPackageTransferCost(update.getMessage().getText());
                    } catch (Exception exception) {
                        SendMessage sendMessage = replyMessagesService.createMessage(UNKNOWN_MAIL_ADDRESS, customer.getId());
                        botMessageService.updateLastMessage(sendMessage, update);
                        break;
                    }
                    if (response.getStatusCodeValue() == 200 && response.getBody() != null) {
                        String cost = response.getBody().getPaynds();
                        cost = (cost.substring(0, cost.length() - 2));
                        String message = MessageFormat.format(MAIL_COST_MESSAGE, cost);
                        SendMessage sendMessage = replyMessagesService.createMessage(message, customer.getId());
                        botMessageService.updateLastMessage(sendMessage, update);
                    } else {
                        SendMessage sendMessage = replyMessagesService.createMessage(UNKNOWN_MAIL_ADDRESS, customer.getId());
                        botMessageService.updateLastMessage(sendMessage, update);
                    }
                    break;
                case BUY:
                    vendorCode = Long.valueOf(update.getMessage().getText());
                    customer.setCurrentCommand(BotCommandEnum.UNKNOWN);
                    customerService.save(customer);
                    try {
                        Product product = productService.findById(vendorCode);
                        String caption = MessageFormat.format("{0} \n Оформить заказ?", productService.createCaption(product));
                        SendPhoto sendPhoto = replyMessagesService.createMessageWithPhoto(product.getPhoto(), caption, customer.getId());
                        String confirmButtonData = MessageFormat.format("{0}{1}", ButtonEnum.CONFIRM_PURCHASE.getCode(), product.getId());
                        ButtonKeyboard buttonKeyboard = new ButtonKeyboard();
                        buttonKeyboard.addMessageButton(0, confirmButtonData, ButtonEnum.CONFIRM_PURCHASE.getName());
                        buttonKeyboard.addMessageButton(1, ButtonEnum.EXIT.getCode(), ButtonEnum.EXIT.getName());
                        sendPhoto.setReplyMarkup(buttonKeyboard.getMessageButtons());
                        botMessageService.updateLastMessage(sendPhoto, update);
                    } catch (RuntimeException exception) {
                        SendMessage sendMessage = replyMessagesService.createMessage(UNKNOWN_VENDOR_CODE, customer.getId());
                        botMessageService.updateLastMessage(sendMessage, update);
                    }
                    break;
                default:
                    botMessageService.deleteUserMessage(customer.getId(), update.getMessage().getMessageId());
                    break;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UpdateType getUpdateHandlerType() {
        return UpdateType.TEXT_MESSAGE;
    }
}
