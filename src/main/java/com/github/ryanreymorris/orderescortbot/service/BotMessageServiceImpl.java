package com.github.ryanreymorris.orderescortbot.service;

import com.github.ryanreymorris.orderescortbot.OrderEscortBot;
import com.github.ryanreymorris.orderescortbot.config.ApplicationContextProvider;
import com.github.ryanreymorris.orderescortbot.entity.Customer;
import com.github.ryanreymorris.orderescortbot.exception.TelegramApiExceptionProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendContact;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Implementation of {@link BotMessageService} interface.
 */
@Service
public class BotMessageServiceImpl implements BotMessageService {

    @Autowired
    private CustomerService customerService;

    @Lazy
    @Autowired
    private TelegramApiExceptionProcessService exceptionProcessService;

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateLastMessage(SendMessage sendMessage, Update update) {
        OrderEscortBot orderEscortBot = getBot();
        Customer customer = customerService.findById(Long.parseLong(sendMessage.getChatId()));
        if (customer.getLastMessage() == null) {
            sendNewMessageToUser(sendMessage, update);
        } else {
            EditMessageText editMessageText = new EditMessageText();
            Message message = update.getMessage() == null ? update.getCallbackQuery().getMessage() : update.getMessage();
            try {
                editMessageText.setChatId(message.getChatId().toString());
                editMessageText.setMessageId(customer.getLastMessage());
                editMessageText.setText(sendMessage.getText());
                editMessageText.setReplyMarkup((InlineKeyboardMarkup) sendMessage.getReplyMarkup());
                orderEscortBot.execute(editMessageText);
            } catch (TelegramApiException exception) {
                exceptionProcessService.processTelegramApiException(editMessageText, exception);
            } finally {
                if (!update.hasCallbackQuery()) {
                    deleteUserMessage(message.getChatId(), message.getMessageId());
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateLastMessage(SendContact sendContact, Update update) {
        OrderEscortBot orderEscortBot = getBot();
        Integer messageId;
        Message message = update.getMessage() == null ? update.getCallbackQuery().getMessage() : update.getMessage();
        try {
            Customer customer = customerService.findById(Long.parseLong(sendContact.getChatId()));
            messageId = orderEscortBot.execute(sendContact).getMessageId();
            customer.setLastMessage(messageId);
            customerService.save(customer);
            //todo add logging to the project.
        } catch (TelegramApiException exception) {
            exceptionProcessService.processTelegramApiException(sendContact, exception);
        } finally {
            deleteUserMessage(message.getChatId(), message.getMessageId());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateLastMessage(SendPhoto sendPhoto, Update update) {
        OrderEscortBot orderEscortBot = getBot();
        Integer messageId;
        Message message = update.getMessage() == null ? update.getCallbackQuery().getMessage() : update.getMessage();
        try {
            Customer customer = customerService.findById(Long.parseLong(sendPhoto.getChatId()));
            messageId = orderEscortBot.execute(sendPhoto).getMessageId();
            deleteUserMessage(message.getChatId(), customer.getLastMessage());
            customer.setLastMessage(messageId);
            customerService.save(customer);
            //todo add logging to the project.
        } catch (TelegramApiException exception) {
            exceptionProcessService.processTelegramApiException(sendPhoto, exception);
        } finally {
            deleteUserMessage(message.getChatId(), message.getMessageId());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendNewMessageToUser(SendMessage sendMessage, Update update) {
        OrderEscortBot orderEscortBot = getBot();
        Message message = update.getMessage() == null ? update.getCallbackQuery().getMessage() : update.getMessage();
        Customer customer = customerService.findById(Long.parseLong(sendMessage.getChatId()));
        try {
            Integer messageId = orderEscortBot.execute(sendMessage).getMessageId();
            customer.setLastMessage(messageId);
            customerService.save(customer);
            //todo add logging to the project.
        } catch (TelegramApiException exception) {
            exceptionProcessService.processTelegramApiException(sendMessage, exception);
        } finally {
            deleteUserMessage(customer.getId(), message.getMessageId());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendNewMessageToUser(SendMessage sendMessage) {
        OrderEscortBot orderEscortBot = getBot();
        try {
            Customer customer = customerService.findById(Long.parseLong(sendMessage.getChatId()));
            Integer messageId = orderEscortBot.execute(sendMessage).getMessageId();
            customer.setLastMessage(messageId);
            customerService.save(customer);
            //todo add logging to the project.
        } catch (TelegramApiException exception) {
            exceptionProcessService.processTelegramApiException(sendMessage, exception);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteUserMessage(Long chatId, Integer messageId) {
        OrderEscortBot orderEscortBot = getBot();
        DeleteMessage deleteMessage = new DeleteMessage();
        try {
            deleteMessage.setChatId(chatId.toString());
            deleteMessage.setMessageId(messageId);
            orderEscortBot.execute(deleteMessage);
            //todo add logging to the project.
        } catch (TelegramApiException exception) {
            exceptionProcessService.processTelegramApiException(deleteMessage, exception);
        }
    }

    /**
     * Get Bot. Resolve cyclic dependencies of beans.
     *
     * @return OrderEscortBot bean.
     */
    private OrderEscortBot getBot() {
        return ApplicationContextProvider.getApplicationContext().getBean(OrderEscortBot.class);
    }
}
