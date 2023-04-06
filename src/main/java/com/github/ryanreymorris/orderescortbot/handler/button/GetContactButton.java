package com.github.ryanreymorris.orderescortbot.handler.button;

import com.github.ryanreymorris.orderescortbot.service.BotMessageService;
import com.github.ryanreymorris.orderescortbot.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendContact;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.github.ryanreymorris.orderescortbot.handler.button.ButtonEnum.GET_CONTACT;

/**
 * Get contact {@link Button}.
 */
@Component
public class GetContactButton implements Button {

    @Autowired
    private ContactService contactService;

    @Autowired
    private BotMessageService botMessageService;

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleClick(Update update) {
        Long userId = update.getCallbackQuery().getMessage().getChatId();
        Long authorId = Long.parseLong(update.getCallbackQuery().getData().replace(GET_CONTACT.getCode(), ""));
        Contact contact = contactService.findById(authorId);
        SendContact sendContact = new SendContact();
        sendContact.setChatId(userId.toString());
        sendContact.setLastName(contact.getLastName());
        sendContact.setFirstName(contact.getFirstName());
        sendContact.setVCard(contact.getVCard());
        sendContact.setPhoneNumber(contact.getPhoneNumber());
        botMessageService.updateLastMessage(sendContact, update);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ButtonEnum getButton() {
        return ButtonEnum.GET_CONTACT;
    }
}
