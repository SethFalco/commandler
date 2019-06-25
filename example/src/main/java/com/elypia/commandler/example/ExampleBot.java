package com.elypia.commandler.example;

import com.elypia.commandler.*;
import com.elypia.commandler.adapters.StringAdapter;
import com.elypia.commandler.controllers.ConsoleController;
import com.elypia.commandler.example.discord.*;
import com.elypia.commandler.loader.AnnotationLoader;
import com.elypia.commandler.metadata.ContextLoader;
import com.elypia.commandler.providers.MiscToStringProvider;
import net.dv8tion.jda.api.*;

import javax.security.auth.login.LoginException;

public class ExampleBot {

    public static void main(String[] args) throws LoginException {
        // Create a loader, this creates an annotation loader which loads from the listed classes.
        AnnotationLoader annoLoader = new AnnotationLoader(
            ExampleModule.class,
            StringAdapter.class,
            MiscToStringProvider.class,
            MiscToMessageProvider.class
        );

        // Create a context providing which loads and provides all static data to Commandler.
        Context context = new ContextLoader(annoLoader).load().build();

        // Create the Commandler instance with our context.
        Commandler commandler = new Commandler(context);

        // Login to Discord so we can accept commands from 2 places.
        JDA jda = new JDABuilder(args[0]).build();

        new ConsoleController(commandler.getDispatchManager());
        new DiscordController(commandler.getDispatchManager(), jda);
    }
}
