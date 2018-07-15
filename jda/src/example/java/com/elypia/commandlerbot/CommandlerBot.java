package com.elypia.commandlerbot;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.pages.PageBuilder;
import com.elypia.commandlerbot.modules.*;
import net.dv8tion.jda.core.*;

import javax.security.auth.login.LoginException;
import java.io.*;

public class CommandlerBot {

    private static final String PREFIX = "!";
    private static final String HELP_URL = "https://commandler.elypia.com/commandler/";

    public static void main(String[] args) throws LoginException, IOException {
        Commandler commandler = new Commandler(PREFIX, HELP_URL);
        commandler.registerModules(new BotModule(), new ExampleModule());

        if (args.length > 0 && args[0].equalsIgnoreCase("-doc"))
            buildDocs(commandler);
        else // In this example we're passing the token via program arguments.
            commandler.setJDA(new JDABuilder(AccountType.BOT).setToken(args[0]).buildAsync());
    }

    private static void buildDocs(Commandler commandler) throws IOException {
        PageBuilder builder = new PageBuilder(commandler);
        builder.setName("CommandlerBot");
        builder.setAvatar("./resources/alexis.png");
        builder.setFavicon("./resources/favicon.ico");
        builder.setDescription("CommandlerBot is the example bot for Commandler!");

        builder.build(String.format(".%spages%<s", File.separator)); // ./pages/
    }
}
