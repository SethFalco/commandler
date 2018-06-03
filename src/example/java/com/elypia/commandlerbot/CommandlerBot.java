package com.elypia.commandlerbot;

import com.elypia.commandler.Commandler;
import com.elypia.commandlerbot.modules.*;
import net.dv8tion.jda.core.*;

import javax.security.auth.login.LoginException;

public class CommandlerBot {

    public static void main(String[] args) throws LoginException {
        JDA jda = new JDABuilder(AccountType.BOT).setToken(args[0]).buildAsync();
        Commandler commandler = new Commandler(jda);

        commandler.registerModules(
            new BotModule(),
            new EmotesModule(),
            new ExampleModule(),
            new GuildModule(),
            new UserModule(),
            new UtilModule(),
            new VoiceModule()
        );
    }
}
