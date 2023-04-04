package com.github.ryanreymorris.orderescortbot.exception;

/**
 * Custom BotException.
 */
public class BotException extends RuntimeException{

    public BotException(String message, Throwable cause) {
        super(message, cause);
    }

    public BotException(String message) {
        super(message);
    }
}
