package com.github.ryanreymorris.orderescortbot.handler;

/**
 * Enumeration of possible user actions
 */
public enum UpdateType {

    /**
     * Text message from user
     */
    TEXT_MESSAGE,

    /**
     * Button click (except main menu buttons)
     */
    BUTTON_CLICK,

    /**
     * Bot command like "/command"
     */
    BOT_COMMAND
}
