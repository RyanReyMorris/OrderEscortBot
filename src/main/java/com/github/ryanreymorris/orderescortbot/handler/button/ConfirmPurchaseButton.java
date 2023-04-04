package com.github.ryanreymorris.orderescortbot.handler.button;

import com.github.ryanreymorris.orderescortbot.entity.Customer;
import com.github.ryanreymorris.orderescortbot.entity.Product;
import com.github.ryanreymorris.orderescortbot.entity.Purchase;
import com.github.ryanreymorris.orderescortbot.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Optional;

import static com.github.ryanreymorris.orderescortbot.handler.button.ButtonEnum.CONFIRM_PURCHASE;

/**
 * Confirm purchase {@link Button}.
 */
@Component
public class ConfirmPurchaseButton implements Button {

    private static final String NO_TEC_SUPPORT = """
            :exclamation: К сожалению, в данный момент нет ни одного сводобного оператора.
            Попробуйте выполнить запрос позже.
            """;

    private static final String SUCCESS_PURCHASE = """
            Ваш заказ был успешно сформирован!
            В ближайшее время с вами свяжется оператор для уточнения деталей заказа.
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
    private ProductService productService;


    @Override
    public void handleClick(Update update) {
        Customer customer = customerService.findCustomerById(update.getCallbackQuery().getFrom().getId());
        List<Purchase> purchaseList = purchaseService.findAllByAuthorId(customer.getId());
        Customer performer;
        if (!purchaseList.isEmpty()) {
            performer = purchaseList.get(0).getPerformer();
        } else {
            Optional<Customer> lessBusyOperator = customerService.findLessBusyTecSupport();
            if (lessBusyOperator.isEmpty()) {
                SendMessage sendMessage = replyMessagesService.createMessage(NO_TEC_SUPPORT, customer.getId());
                botMessageService.updateLastMessage(sendMessage, update);
                return;
            } else {
                performer = lessBusyOperator.get();
            }
        }
        Long productId = Long.parseLong(update.getCallbackQuery().getData().replace(CONFIRM_PURCHASE.getCode(), ""));
        Product product = productService.findProductById(productId);
        Purchase purchase = new Purchase();
        purchase.setAuthor(customer);
        purchase.setProduct(product);
        purchase.setPerformer(performer);
        purchaseService.save(purchase);
        SendMessage sendMessage = replyMessagesService.createMessage(SUCCESS_PURCHASE, customer.getId());
        botMessageService.updateLastMessage(sendMessage, update);
    }

    @Override
    public ButtonEnum getButton() {
        return ButtonEnum.CONFIRM_PURCHASE;
    }
}
