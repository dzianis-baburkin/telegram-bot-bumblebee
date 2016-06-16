package com.github.bumblebee.command.autocomplete;

import com.github.bumblebee.command.SingleArgumentCommand;
import com.github.bumblebee.security.PrivilegedCommand;
import com.github.bumblebee.security.UnauthorizedRequestAware;
import com.github.bumblebee.security.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import telegram.api.BotApi;
import telegram.domain.Update;


@PrivilegedCommand(name = "AutoCompleteAddCommand", role = UserRole.MODERATOR)
public class AutoCompleteAddCommand extends SingleArgumentCommand implements UnauthorizedRequestAware {

    private final BotApi botApi;
    private final AutoCompleteHandler handler;

    @Autowired
    public AutoCompleteAddCommand(BotApi botApi, AutoCompleteHandler handler) {
        this.botApi = botApi;
        this.handler = handler;
    }

    @Override
    public void handleCommand(Update update, Long chatId, String argument) {

        if (!handler.addTemplate(argument.trim())) {
            botApi.sendMessage(chatId, "Wrong template, try again.");
            return;
        }

        botApi.sendMessage(chatId, "Pattern successfully added.");
    }

    @Override
    public void onUnauthorizedRequest(Update update) {
        botApi.sendMessage(update.getMessage().getChat().getId(),
                "You are not allowed to execute this command. Become a moderator to do this.");
    }
}
