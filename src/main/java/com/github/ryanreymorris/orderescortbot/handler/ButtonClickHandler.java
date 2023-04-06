package com.github.ryanreymorris.orderescortbot.handler;

import com.github.ryanreymorris.orderescortbot.handler.button.Button;
import com.github.ryanreymorris.orderescortbot.handler.button.ButtonEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

/**
 * Handler of button click action.
 */
@Component
public class ButtonClickHandler implements UpdateHandler {

    @Autowired
    private Map<ButtonEnum, Button> buttons;

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(Update update) {
        String buttonData = update.getCallbackQuery().getData();
        if (buttonData.startsWith(ButtonEnum.TEC_SUPPORT.getCode())) {
            buttons.get(ButtonEnum.TEC_SUPPORT).handleClick(update);
        } else if (buttonData.startsWith(ButtonEnum.PURCHASE_AUTHOR.getCode())) {
            buttons.get(ButtonEnum.PURCHASE_AUTHOR).handleClick(update);
        } else if (buttonData.startsWith(ButtonEnum.CONFIRM_PURCHASE.getCode())) {
            buttons.get(ButtonEnum.CONFIRM_PURCHASE).handleClick(update);
        } else if (buttonData.startsWith(ButtonEnum.DELETE_AUTHOR_PURCHASES.getCode())) {
            buttons.get(ButtonEnum.DELETE_AUTHOR_PURCHASES).handleClick(update);
        } else if (buttonData.startsWith(ButtonEnum.GET_CONTACT.getCode())) {
            buttons.get(ButtonEnum.GET_CONTACT).handleClick(update);
        } else if (buttonData.startsWith(ButtonEnum.DELETE_TS_REQUEST.getCode())) {
            buttons.get(ButtonEnum.DELETE_TS_REQUEST).handleClick(update);
        } else {
            ButtonEnum button = ButtonEnum.valueOf(buttonData);
            buttons.get(button).handleClick(update);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UpdateType getUpdateHandlerType() {
        return UpdateType.BUTTON_CLICK;
    }
}
