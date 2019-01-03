package com.elypia.commandler.interfaces;

import com.elypia.commandler.*;

/**
 * The {@link ICommandProcessor} has the role of processing an event
 * from your respective platform into something that can be interperted
 * by Commandler, this should be used to verify if it's a command as well
 * parse it into an input and event object to be used internally.
 *
 * @param <S> The (S)ource event that triggered this.
 * @param <M> The (M)essage type.
 */
public interface ICommandProcessor<S, M> {

    /**
     * Receieve and handles the event.
     *
     * @param source The event spawned by the client.
     * @param content The content of the message to parse.
     * @param send If we should send this message.
     * @return The response to this command, or null
     * if this wasn't a command at all.
     */
    M dispatch(S source, String content, boolean send);

    /**
     * Check if this event is a command at all.
     *
     * @param source The source event.
     * @param content The message to check.
     * @return If this was a command.
     */
    boolean isCommand(S source, String content);

    ICommandEvent<S, M> spawnEvent(Commandler<S, M> commandler, S source, CommandInput input);

    /**
     * Break the command down into it's individual components.
     *
     * @param event The event spanwed by the client.
     * @param content The content of the meessage.
     * @return The input the user provided or null if it's not a valid command.
     */
    ICommandEvent process(Commandler<S, M> commandler, S event, String content);

    /**
     * Return the accepted prefixes for this event.
     *
     * @param event
     * @return
     */
    String[] getPrefixes(S event);
}
