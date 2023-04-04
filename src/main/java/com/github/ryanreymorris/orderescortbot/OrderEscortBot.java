package com.github.ryanreymorris.orderescortbot;

import com.github.ryanreymorris.orderescortbot.facade.BotFacadeService;
import com.github.ryanreymorris.orderescortbot.handler.command.BotCommandEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class OrderEscortBot extends TelegramLongPollingBot {

    @Value("${bot.token}")
    private String token;

    @Value("${bot.username}")
    private String username;

    @Autowired
    private BotFacadeService botFacadeService;

    @Override
    public void onUpdateReceived(Update update) {
        botFacadeService.handleUpdate(update);
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onRegister() {
        super.onRegister();
        List<BotCommand> botCommands = new ArrayList<>();
        for (BotCommandEnum commandEnum : BotCommandEnum.values()) {
            if (StringUtils.isNotEmpty(commandEnum.getCommandDescription())) {
                BotCommand botCommand = new BotCommand(commandEnum.getCommandName(), commandEnum.getCommandDescription());
                botCommands.add(botCommand);
            }
        }
        try {
            this.execute(new SetMyCommands(botCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            //todo log
        }
    }
}
