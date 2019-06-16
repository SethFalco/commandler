package com.elypia.commandler.interfaces;

import com.elypia.commandler.*;
import com.elypia.commandler.exceptions.misuse.*;

/**
 * The {@link Dispatcher} has the role of processing an event
 * from your respective platform into something that can be interperted
 * by Commandler, this should be used to verify if it's a command as well
 * adapt it into an input and event object to be used internally.
 *
 * @param <S> The (S)ource event that triggered this.
 * @param <M> The (B)essage type.
 */
public interface Dispatcher<S, M> {

    /**
     * Receieve and handles the event.
     *
     * @param source The event spawned by the client.
     * @param content The content of the message to adapt.
     * @param send If we should send this message.
     * @return The response to this command, or null
     * if this wasn't a command at all.
     */
    M dispatch(S source, String content, boolean send);

    /**
     * Check if this event is a command at all and returns
     * the prefix used if so.
     *
     * @param source The source event.
     * @param content The message to check.
     * @return The prefix used if this is a command, else null.
     */
    String isValid(S source, String content);

    /**
     * Break the command down into it's individual components.
     *
     * @param event The event spanwed by the client.
     * @param content The content of the meessage.
     * @return The input the user provided or null if it's not a valid command.
     */
    CommandlerEvent parse(Commandler commandler, S event, String content) throws OnlyPrefixException, NoDefaultCommandException, ModuleNotFoundException;
}
