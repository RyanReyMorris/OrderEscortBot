package com.github.ryanreymorris.orderescortbot.config;

import com.github.ryanreymorris.orderescortbot.handler.button.Button;
import com.github.ryanreymorris.orderescortbot.handler.button.ButtonEnum;
import com.github.ryanreymorris.orderescortbot.handler.command.BotCommandEnum;
import com.github.ryanreymorris.orderescortbot.handler.command.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Bot buttons configuration.
 */
@Configuration
public class ButtonsConfig {

    @Autowired
    private List<Button> buttons;

    @Bean
    public Map<ButtonEnum, Button> buttons() {
        Map<ButtonEnum, Button> botButtons = new HashMap<>();
        for (Button button : buttons) {
            botButtons.put(button.getButton(), button);
        }
        return botButtons;
    }

}
