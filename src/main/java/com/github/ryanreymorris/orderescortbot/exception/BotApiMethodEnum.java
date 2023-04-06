package com.github.ryanreymorris.orderescortbot.exception;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

/**
 * Enumeration of {@link BotApiMethod}.
 *
 * @see TelegramApiExceptionProcessService
 */
public enum BotApiMethodEnum {
    DeleteMessage,
    SendMessage,
    SendContact,
    SendPhoto,
    EditMessageText
}
