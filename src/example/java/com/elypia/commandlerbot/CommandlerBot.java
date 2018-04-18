package com.elypia.commandlerbot;

import com.elypia.commandler.jda.JDACommandler;
import com.elypia.commandlerbot.modules.*;
import net.dv8tion.jda.core.*;

import javax.security.auth.login.LoginException;

public class CommandlerBot {

    private static final String token = "NDM2MTAwNjQyMzEwNzE3NDQw.DbimdQ.lyK7YXuHxR8UJpqZXYHyQClMUyM";

    public static void main(String[] args) throws LoginException {
        JDA jda = new JDABuilder(AccountType.BOT).setToken(token).buildAsync();
        JDACommandler commandler = new JDACommandler(jda);

        commandler.registerModules(
            new BotModule(),
            new GuildModule()
        );
    }
}
