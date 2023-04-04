package com.github.ryanreymorris.orderescortbot.exception;

import org.telegram.telegrambots.meta.api.methods.send.SendContact;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Service for telegram api exceptions processing.
 */
public interface TelegramApiExceptionProcessService {

    /**
     * Process telegram-api exception
     */
    void processTelegramApiException(SendPhoto sendPhoto, TelegramApiException exception);

    /**
     * Process telegram-api exception
     */
    void processTelegramApiException(DeleteMessage deleteMessage, TelegramApiException exception);

    /**
     * Process telegram-api exception
     */
    void processTelegramApiException(SendMessage sendMessage, TelegramApiException exception);

    /**
     * Process telegram-api exception
     */
    void processTelegramApiException(SendContact sendContact, TelegramApiException exception);

    /**
     * Process telegram-api exception
     */
    void processTelegramApiException(EditMessageText editMessageText, TelegramApiException exception);
}
