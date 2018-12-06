package com.elypia.commandler.console;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.console.client.*;

public class StringConfiler extends Confiler<StringClient, StringEvent, String> {

    public StringConfiler(String... prefixes) {
        this(prefixes, null);
    }

    public StringConfiler(String[] prefixes, String help) {
        super(prefixes, help);
    }

    /**
     * Break the command down into it's individual components.
     *
     * @param event The event spanwed by the client.
     * @param content The content of the meessage.
     * @return The input the user provided or null if it's not a valid command.
     */
    @Override
    public StringCommand processEvent(Commandler<StringClient, StringEvent, String> commandler, StringEvent event, String content) {
        var parent = super.processEvent(commandler, event, content);
        return new StringCommand(parent);
    }
}
