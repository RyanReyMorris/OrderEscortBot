package com.github.ryanreymorris.orderescortbot;

import com.github.ryanreymorris.orderescortbot.facade.BotFacadeService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.starter.SpringWebhookBot;

@Slf4j
@Getter
@Setter
public class OrderEscortBot extends SpringWebhookBot {

    private String botPath;

    private String botName;

    private String botToken;

    @Autowired
    private BotFacadeService botFacadeService;

    public OrderEscortBot(SetWebhook setWebhook) {
        super(setWebhook);
    }

    public OrderEscortBot(DefaultBotOptions options, SetWebhook setWebhook) {
        super(options, setWebhook);
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return botFacadeService.handleUpdate(update);
    }

    @Override
    public String getBotUsername() {
        return getBotName();
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }
}
