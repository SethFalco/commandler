package com.elypia.commandlerbot;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.pages.PageBuilder;
import com.elypia.commandlerbot.modules.*;
import net.dv8tion.jda.core.*;

import javax.security.auth.login.LoginException;
import java.io.*;

public class CommandlerBot {

    public static void main(String[] args) throws LoginException, IOException {
        boolean doc = args.length > 0 && args[0].equalsIgnoreCase("-doc");
        JDA jda = doc ? null : new JDABuilder(AccountType.BOT).setToken(args[0]).buildAsync();

        Commandler commandler = new Commandler(jda, "!");

        commandler.registerModules(
            new BotModule(),
            new EmotesModule(),
            new ExampleModule(),
            new GuildModule(),
            new UserModule(),
            new UtilModule(),
            new VoiceModule()
        );

        if (doc) {
            PageBuilder builder = new PageBuilder(commandler);
            builder.setName("CommandlerBot");
            builder.setAvatar("./assets/alexis.png");
            builder.setFavicon("./assets/favicon.ico");
            builder.setDescription("CommandlerBot is the example bot for Commandler!");

            builder.build(new File("." + File.separator + "pages" + File.separator));
        }
    }
}
