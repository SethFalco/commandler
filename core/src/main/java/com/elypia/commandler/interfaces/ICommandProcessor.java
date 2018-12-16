package com.elypia.commandler.interfaces;

import com.elypia.commandler.Commandler;

import java.util.regex.Matcher;

public interface ICommandProcessor<E, M> {

    /**
     * This should process the event.
     *
     * @param source The event spawned by the client.
     * @param content The content of the message to parse.
     * @return The message that was sent to the client.
     */
    M dispatch(E source, String content, boolean send);

    Matcher isCommand(E source, String content);

    /**
     * Break the command down into it's individual components.
     *
     * @param event The event spanwed by the client.
     * @param content The content of the meessage.
     * @return The input the user provided or null if it's not a valid command.
     */
    ICommandEvent process(Commandler<E, M> commandler, E event, String content);

    /**
     * Return the accepted prefixes for this event.
     *
     * @param event
     * @return
     */
    String[] getPrefixes(E event);
}
