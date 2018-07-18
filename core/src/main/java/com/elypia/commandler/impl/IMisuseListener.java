package com.elypia.commandler.impl;

import com.elypia.commandler.*;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.metadata.*;

import java.util.List;

/**
 * Whenever user errors occur such as using the bot incorrectly (<strong>not exceptions</strong>)
 * these methods are called to provide a friendly error message to the user.
 *
 * @param <M></M> The type of message that the client expects to send.
 */


public interface IMisuseListener<M> {

    /**
     * This may occur when we've found the command the user wanted to perform
     * but the command and none of it's overloads allow that number of parameters.
     *
     * @return The friendly error to send to users in chat.
     */

    M parameterCountMismatch(CommandInput input, MetaCommand metaCommand);

    /**
     * This occurs when it looks like the user had attempted to perform a
     * {@link Default} command however the {@link Module} they specified doesn't
     * actually own a {@link Default} command.
     *
     * @return The friendly error to send to users in chat.
     */

    M noDefaultCommand();

    M noParser(CommandEvent event, Class<?> type, String input);

    /**
     * This may occur whenever user input fails to parse.
     *
     * @param event The event that caused this failure.
     * @param type The data type we were required.
     * @param input The input the user provided.
     * @return The friendly error to send to users in chat.
     */

    M parameterParseFailure(CommandEvent event, Class<?> type, String input);

    /**
     *
     * @param event
     * @param param
     * @param items
     * @return
     */

    M unsupportedList(CommandEvent event, MetaParam param, List<String> items);

    M commandInvalidated();

    M parameterInvalidated();

    /**
     * This may occur when a user attempts to perform a command
     * which isn't the {@link IHandler#help(CommandEvent)} command on
     * a {@link Module} that has been disbaled.
     *
     * @param event The event that caused this failure.
     * @return The friendly error to send to users in chat.
     */

    M moduleDisabled(CommandEvent event);

    /**
     * This will occur if an {@link ReflectiveOperationException} exception occurs
     * when attempting to perform the command, while this should never happen thanks
     * to validation performed by {@link Commandler} this event is called
     * should it ever occur.
     *
     * @param ex The exception that occured.
     * @return The friendly error to send to users in chat.
     */

    M exceptionFailure(ReflectiveOperationException ex);
}
