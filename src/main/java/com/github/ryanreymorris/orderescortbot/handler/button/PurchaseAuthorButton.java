package com.github.ryanreymorris.orderescortbot.handler.button;

import com.github.ryanreymorris.orderescortbot.entity.Customer;
import com.github.ryanreymorris.orderescortbot.entity.Product;
import com.github.ryanreymorris.orderescortbot.entity.Purchase;
import com.github.ryanreymorris.orderescortbot.service.BotMessageService;
import com.github.ryanreymorris.orderescortbot.service.CustomerService;
import com.github.ryanreymorris.orderescortbot.service.PurchaseService;
import com.github.ryanreymorris.orderescortbot.service.ReplyMessagesService;
import com.github.ryanreymorris.orderescortbot.storage.PaginationStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.text.MessageFormat;
import java.util.List;

import static com.github.ryanreymorris.orderescortbot.handler.button.ButtonEnum.PURCHASE_AUTHOR;

/**
 * Purchase Author {@link Button}.
 */
@Component
public class PurchaseAuthorButton implements Button {

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
    public void handleClick(Update update) {
        Customer performer = customerService.findById(update.getCallbackQuery().getFrom().getId());
        storage.resetPagination(performer.getId());
        Long authorId = Long.parseLong(update.getCallbackQuery().getData().replace(PURCHASE_AUTHOR.getCode(), ""));
        List<Purchase> purchaseList = purchaseService.findAllByAuthorId(authorId);
        String messageText = getAuthorPurchasesList(purchaseList, customerService.findById(authorId).getUserName());
        String getButtonData = MessageFormat.format("{0}{1}", ButtonEnum.GET_CONTACT.getCode(), authorId.toString());
        String deleteButtonData = MessageFormat.format("{0}{1}", ButtonEnum.DELETE_AUTHOR_PURCHASES.getCode(), authorId.toString());
        ButtonKeyboard buttonKeyboard = new ButtonKeyboard();
        buttonKeyboard.addMessageButton(0, getButtonData, ButtonEnum.GET_CONTACT.getName());
        buttonKeyboard.addMessageButton(1, deleteButtonData, ButtonEnum.DELETE_AUTHOR_PURCHASES.getName());
        buttonKeyboard.addMessageButton(2, ButtonEnum.EXIT.getCode(), ButtonEnum.EXIT.getName());
        SendMessage sendMessage = replyMessagesService.createMessageWithButtons(messageText, performer.getId(), buttonKeyboard.getMessageButtons());
        botMessageService.updateLastMessage(sendMessage, update);
    }

    /**
     * Create start message.
     *
     * @return start message.
     */
    private static String getAuthorPurchasesList(List<Purchase> purchaseList, String authorUsername) {
        StringBuilder purchases = new StringBuilder();
        purchases.append(MessageFormat.format("Ниже перечислены все заказы пользователя {0}: \n", authorUsername));
        for (int i = 0; i < purchaseList.size(); i++) {
            Product product = purchaseList.get(i).getProduct();
            String purchaseName = MessageFormat.format("Артикул:{0}, Название:{1}", product.getId(), product.getName());
            purchases.append(MessageFormat.format("{0}: {1}; \n", i+1, purchaseName));
        }
        return purchases.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ButtonEnum getButton() {
        return ButtonEnum.PURCHASE_AUTHOR;
    }
}
