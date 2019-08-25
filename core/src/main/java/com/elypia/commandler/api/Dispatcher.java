package com.elypia.commandler.api;

import com.elypia.commandler.event.ActionEvent;

/**
 * The {@link Dispatcher} has the role of processing an event
 * from your respective platform into something that can be interperted
 * by Commandler, this should be used to verify if it's a command as well
 * adapt it into an input and event object to be used internally.
 */
public interface Dispatcher {

    /**
     * @param event The source event that called this command.
     * @param content The content of the command.
     * @return If this is a valid command or not.
     * This does not mean it is a command, it's just for checking if the text
     * is formatted in a way that it fits the formatting of a command.
     */
    boolean isValid(Object event, String content);

    /**
     * Break the command down into it's individual components.
     *
     * @param integration The controller that dispatched this event.
     * @param content The content of the meessage.
     * @return The input the user provided or null if it's not a valid command.
     */
    <S, M> ActionEvent<S, M> parse(Integration<S, M> integration, S source, String content);
}
