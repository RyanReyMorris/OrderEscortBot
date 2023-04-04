package com.github.ryanreymorris.orderescortbot.exception;

import com.github.ryanreymorris.orderescortbot.service.BotMessageService;
import com.github.ryanreymorris.orderescortbot.service.ReplyMessagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendContact;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

/**
 * Implementation of {@link TelegramApiExceptionProcessService}.
 */
@Service
public class TelegramApiExceptionProcessServiceImpl implements TelegramApiExceptionProcessService {
    /**
     * Throws if we are trying to update the same message. Solution: just delete user's message and do nothing.
     */
    private static final String UPDATE_SAME_MESSAGE_EXCEPTION = "Bad Request: message is not modified: specified new message content and reply markup are exactly the same as a current content and reply markup of the message";

    private static final String UPDATE_MESSAGE_WITH_MENU_BUTTONS = "Bad Request: message can't be edited";

    private static final String UPDATE_MESSAGE_WITH_PHOTO = "Bad Request: there is no text in the message to edit";

    @Lazy
    @Autowired
    private BotMessageService botMessageService;

    @Autowired
    private ReplyMessagesService replyMessagesService;

    @Override
    public void processTelegramApiException(DeleteMessage deleteMessage, TelegramApiException exception) {
        exception.printStackTrace();
    }

    @Override
    public void processTelegramApiException(SendMessage sendMessage, TelegramApiException exception) {
        exception.printStackTrace();
    }

    @Override
    public void processTelegramApiException(SendContact sendContact, TelegramApiException exception) {
        exception.printStackTrace();
    }

    @Override
    public void processTelegramApiException(SendPhoto sendPhoto, TelegramApiException exception) {
        exception.printStackTrace();
    }

    @Override
    public void processTelegramApiException(EditMessageText editMessageText, TelegramApiException exception) {
        if (exception instanceof TelegramApiRequestException apiRequestException) {
            processTelegramApiRequestException(editMessageText, apiRequestException);
        } else {
            exception.printStackTrace();
        }
    }

    /**
     * Process {@link TelegramApiRequestException}.
     *
     * @param editMessageText     - editMessageText that cause exception.
     * @param apiRequestException - exception.
     */
    private void processTelegramApiRequestException(EditMessageText editMessageText, TelegramApiRequestException apiRequestException) {
        switch (apiRequestException.getApiResponse()) {
            case UPDATE_SAME_MESSAGE_EXCEPTION:
                break;
            case UPDATE_MESSAGE_WITH_MENU_BUTTONS:
            case UPDATE_MESSAGE_WITH_PHOTO:
                botMessageService.deleteUserMessage(Long.parseLong(editMessageText.getChatId()), editMessageText.getMessageId());
                SendMessage sendMessage = replyMessagesService.createMessage(editMessageText.getText(), Long.parseLong(editMessageText.getChatId()));
                sendMessage.setReplyMarkup(editMessageText.getReplyMarkup());
                botMessageService.sendNewMessageToUser(sendMessage);
                break;
            default:
                apiRequestException.printStackTrace();
                break;
        }
    }
}
