package com.github.ryanreymorris.orderescortbot.handler.command;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration for {@link Command}'s.
 */
public enum BotCommandEnum {

    START("/start", null),
    INFO("/info", "информация о товаре"),
    DELIVERY("/delivery", "стоимость и срок доставки"),
    BUY("/buy", "оформить заказ"),
    OTHER("/other", "помощь оператора"),
    HELP("/help", "помощь"),
    UNKNOWN(null, null);

    private final String commandName;

    private final String commandDescription;

    BotCommandEnum(String commandName, String commandDescription) {
        this.commandName = commandName;
        this.commandDescription = commandDescription;
    }

    public String getCommandName() {
        return commandName;
    }

    public String getCommandDescription() {
        return commandDescription;
    }

    private static final Map<String, BotCommandEnum> map;
    static {
        map = new HashMap<>();
        for (BotCommandEnum v : BotCommandEnum.values()) {
            map.put(v.commandName, v);
        }
    }
    public static BotCommandEnum findByCommandName(String commandName) {
        return map.getOrDefault(commandName, UNKNOWN);
    }
}
