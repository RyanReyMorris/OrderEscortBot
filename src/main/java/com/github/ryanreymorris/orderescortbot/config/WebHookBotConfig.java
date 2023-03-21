package com.github.ryanreymorris.orderescortbot.config;

import com.github.ryanreymorris.orderescortbot.OrderEscortBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;

@Configuration
public class WebHookBotConfig {

    @Autowired
    private OrderEscortBotConfig botConfig;

    @Bean
    public SetWebhook setWebhookInstance() {
        return SetWebhook.builder().url(botConfig.getBotPath()).build();
    }

    @Bean
    public OrderEscortBot springWebhookBot(SetWebhook setWebhook) {
        OrderEscortBot bot = new OrderEscortBot(setWebhook);
        bot.setBotToken(botConfig.getBotToken());
        bot.setBotName(botConfig.getBotName());
        bot.setBotPath(botConfig.getBotPath());
        return bot;
    }
}