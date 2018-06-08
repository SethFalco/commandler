package com.elypia.commandlerbot;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.pages.PageBuilder;
import com.elypia.commandlerbot.modules.*;
import net.dv8tion.jda.core.*;

import javax.security.auth.login.LoginException;
import java.io.*;

public class CommandlerBot {

    public static void main(String[] args) throws LoginException, IOException {
        boolean doc = isDocPresent(args);
        JDA jda = null;

        if (!doc)
            jda = new JDABuilder(AccountType.BOT).setToken(args[0]).buildAsync();

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

        if (doc)
            generateDocumentation(commandler, new File(args[2]));
    }

    private static boolean isDocPresent(String[] args) {
        if (args.length > 2)
            return args[1].equalsIgnoreCase("-doc");

        return false;
    }

    private static void generateDocumentation(Commandler commandler, File file) throws IOException {
        PageBuilder builder = new PageBuilder(commandler);
        builder.setName("CommandlerBot");
        builder.setAvatar("./assets/alexis.png");
        builder.setFavicon("./assets/favicon.ico");
        builder.setDescription("CommandlerBot is the example bot for Commandler!");

        builder.build(new File("." + File.separator + "public" + File.separator));
    }
}
