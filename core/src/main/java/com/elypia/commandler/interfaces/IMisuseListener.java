package com.elypia.commandler.interfaces;

import com.elypia.commandler.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.impl.*;
import com.elypia.commandler.metadata.*;

import javax.validation.ConstraintViolation;
import java.util.*;

/**
 * Whenever user errors occur such as using the bot incorrectly (<strong>not exceptions</strong>)
 * these methods are called to provide a friendly error message to the user.
 *
 * The return types are {@link Object} as we will use the {@link IBuilder}
 * internally to generate whatever should be sent.
 */
public interface IMisuseListener<C, E, M> {

    /**
     * This will occur when the user attempts to do a command
     * where the root alias doesn't match any of our
     * registered modules or static commands what so ever.
     *
     * @return The friendly error to send to the user.
     */
    Object onModuleNotFound(String content);

    /**
     * This may occur when we've found the command the user wanted to perform
     * but the command and none of it's overloads allow that number of parameters.
     *
     * @return The friendly error to send to users in chat.
     */
    Object onParamCountMismatch(CommandInput input, CommandData<?, ?, ?> commandData);

    /**
     * This occurs when it looks like the user had attempted to perform a
     * {@link Default} command however the {@link Module} they specified doesn't
     * actually own a {@link Default} command.
     *
     * @return The friendly error to send to users in chat.
     */
    Object onDefaultNotFound(ICommandEvent event);

    /**
     * This may occur whenever user input fails to parse as into
     * the intended object.
     *
     * @param event The event that caused this failure.
     * @param type The data type we were required.
     * @param item The input the user provided.
     * @return The friendly error to send to users in chat.
     */
    Object onParamParseFailure(ICommandEvent event, ParamData paramData, Class<?> type, String item);

    /**
     * This may occur whenever the user has specified list
     * parameters however a list is not acceptable here.
     *
     * @param event
     * @param paramData
     * @param items
     * @return
     */
    Object onListNotSupported(ICommandEvent event, ParamData paramData, List<String> items);

    /**
     * This occurs when an {@link ICommandValidator} invalidates the event.
     *
     * @return
     */
    Object onCommandInvalidated(ICommandEvent<C, E, M> event, CommandData commandData, ICommandValidator validator);

    /**
     * This occurs when an {@link IParamValidator} invalidates a parameter on the command.
     *
     * @return
     */
    <H extends IHandler<C, E, M>> Object onParamInvalidated(ICommandEvent<C, E, M> event, Set<ConstraintViolation<H>> violations);

    /**
     * This may occur when a user attempts to perform a command
     * which isn't the {@link IHandler#help(ICommandEvent)} command on
     * a {@link Module} that has been disabled.
     *
     * @param event The event that caused this failure.
     * @return The friendly error to send to users in chat.
     */
    Object onModuleDisabled(ICommandEvent<C, E, M> event);

    /**
     * This will occur if an {@link Exception exception} occurs
     * when attempting to perform the command. This would normally due to a
     * module having an uncaught exception but is also a fall back in case
     * there is a bug in {@link Commandler}.
     *
     * It is <strong>NOT</strong> recommended to print raw exceptions
     * to users, this is a terrible idea and if you planned on doing that
     * you are bad and you should feel bad.
     *
     * @param ex The exception that occured.
     * @return The friendly error to send to users in chat.
     */
     <X extends Exception> Object onException(X ex);
}
