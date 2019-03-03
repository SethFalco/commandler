package com.elypia.commandler.interfaces;

import com.elypia.commandler.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.metadata.data.ParamData;

import javax.validation.ConstraintViolation;
import javax.validation.executable.ExecutableValidator;
import java.util.*;

/**
 * Whenever user errors occur such as using the bot incorrectly (<strong>not exceptions</strong>)
 * these methods are called to provide a friendly error message to the user.
 *
 * The return types are {@link Object} as we will use the {@link Builder}
 * internally to generate whatever should be sent.
 */
public interface MisuseListener<S, M> {

    /**
     * This will occur when the user attempts to do a command
     * where the root alias doesn't match any of our
     * registered modules or static commands what so ever.
     *
     * @return The friendly error to send to the user.
     */
    String onModuleNotFound(String content);

    /**
     * This may occur when we've found the command the user wanted to perform
     * but the command and none of it's overloads allow that number of parameters.
     *
     * @return The friendly error to send to users in chat.
     */
    String onParamCountMismatch(EventInput input);

    /**
     * This occurs when it looks like the user had attempted to perform a
     * {@link Default} command however the {@link Module} they specified doesn't
     * actually own a {@link Default} command.
     *
     * @return The friendly error to send to users in chat.
     */
    String onDefaultNotFound(CommandlerEvent event);

    /**
     * This may occur whenever user input fails to parse as into
     * the intended object.
     *
     * @param event The event that caused this failure.
     * @param type The data type we were required.
     * @param item The input the user provided.
     * @return The friendly error to send to users in chat.
     */
    String onParamParseFailure(CommandlerEvent event, ParamData paramData, Class<?> type, String item);

    /**
     * This may occur whenever the user has specified list
     * parameters however a list is not acceptable here.
     *
     * @param event
     * @param paramData
     * @param items
     * @return
     */
    String onListNotSupported(CommandlerEvent event, ParamData paramData, List<String> items);

    /**
     * This occurs when the {@link ExecutableValidator} invalidates
     * one of the parameters provided by the user.
     *
     * @return The friendly error to send to the users in chat.
     */
    <H extends Handler<S, M>> String onInvalidated(CommandlerEvent<S, M> event, Set<ConstraintViolation<H>> violations);

    /**
     * This may occur when a user attempts to perform a command
     * which isn't the {@link Handler#help(CommandlerEvent)} command on
     * a {@link Module} that has been disabled.
     *
     * @param event The event that caused this failure.
     * @return The friendly error to send to users in chat.
     */
    String onModuleDisabled(CommandlerEvent<S, M> event);

    /**
     * This will occur if an {@link Exception exception} occurs
     * when attempting to perform the command. This would normally due to a
     * module having an uncaught exception but is also a fall back in case
     * there is a bug in {@link Commandler}.
     *
     * @param ex The exception that occured.
     * @return The friendly error to send to users in chat.
     */
     <X extends Exception> String onException(X ex);
}
