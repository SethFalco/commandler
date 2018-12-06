package com.elypia.commandler.interfaces;

import com.elypia.commandler.*;
import com.elypia.commandler.annotations.Default;
import com.elypia.commandler.impl.*;
import com.elypia.commandler.metadata.*;

import java.util.*;
import java.util.regex.*;

public interface ICommandProcessor<C, E, M> {

    /**
     * This should process the event.
     *
     * @param source The event spawned by the client.
     * @param content The content of the message to parse.
     * @return The message that was sent to the client.
     */
    public M dispatch(E source, String content, boolean send);

    /**
     * Break the command down into it's individual components.
     *
     * @param event The event spanwed by the client.
     * @param content The content of the meessage.
     * @return The input the user provided or null if it's not a valid command.
     */
    public CommandEvent process(Commandler<C, E, M> commandler, E event, String content);
}
