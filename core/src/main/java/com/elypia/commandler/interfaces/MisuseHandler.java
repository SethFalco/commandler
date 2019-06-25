package com.elypia.commandler.interfaces;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.exceptions.*;

import javax.validation.executable.ExecutableValidator;

/**
 * Whenever user errors occur such as using the bot incorrectly (<strong>not exceptions</strong>)
 * these methods are called to provide a friendly error message to the user.
 *
 * The return types are {@link Object} as we will use the {@link ResponseProvider}
 * internally to generate whatever should be sent.
 */
// TODO: Change Misuse to not handled a fix set of exceptions, but be a collection of exceptions and
// TODO: What object to return from it.
public interface MisuseHandler {

    /**
     * Route the exception to the correct block.
     *
     * @param ex The exception that occured.
     * @param <T> The type of exception.
     */
    default <T extends Exception> Object route(T ex) {
        if (ex instanceof OnlyPrefixException)
            return onOnlyPrefix((OnlyPrefixException)ex);
        else if (ex instanceof ModuleNotFoundException)
            return onModuleNotFound((ModuleNotFoundException)ex);
        else if (ex instanceof ParamCountMismatchException)
            return onParamMismatch((ParamCountMismatchException)ex);
        else if (ex instanceof NoDefaultCommandException)
            return onNoDefaultCommand((NoDefaultCommandException)ex);
        else if (ex instanceof ParamParseException)
            return onParamParse((ParamParseException)ex);
        else if (ex instanceof ListUnsupportedException)
            return onListUnsupported((ListUnsupportedException)ex);
        else if (ex instanceof ParamViolationException)
            return onParamViolation((ParamViolationException)ex);
        else if (ex instanceof ModuleDisabledException)
            return onDisabled((ModuleDisabledException)ex);
        else
            return onException(ex);
    }

    /**
     * When the prefix was used, but no other context was
     * specified.
     *
     * @param ex The exception that occured.
     * @return The friendly error to send to the user.
     */
    Object onOnlyPrefix(OnlyPrefixException ex);

    /**
     * This will occur when the user attempts to do a command
     * where the root alias doesn't match any of our
     * registered modules or static commands what so ever.
     *
     * @param ex The exception that occured.
     * @return The friendly error to send to the user.
     */
    Object onModuleNotFound(ModuleNotFoundException ex);

    /**
     * This may occur when we've found the command the user wanted to perform
     * but the command and none of it's overloads allow that number of parameters.
     *
     * @param ex The exception that occured.
     * @return The friendly error to send to users in chat.
     */
    Object onParamMismatch(ParamCountMismatchException ex);

    /**
     * This occurs when it looks like the user had attempted to perform a
     * {@link Default} command however the {@link Module} they specified doesn't
     * actually own a {@link Default} command.
     *
     * @return The friendly error to send to users in chat.
     */
    Object onNoDefaultCommand(NoDefaultCommandException ex);

    /**
     * This may occur whenever user input fails to adapt as into
     * the intended object.
     *
     * @param ex The exception that occured.
     * @return The friendly error to send to users in chat.
     */
    Object onParamParse(ParamParseException ex);

    /**
     * This may occur whenever the user has specified list
     * parameters however a list is not acceptable here.
     *
     * @param ex The exception that occured.
     * @return
     */
    Object onListUnsupported(ListUnsupportedException ex);

    /**
     * This occurs when the {@link ExecutableValidator} invalidates
     * one of the parameters provided by the user.
     *
     * @param ex The exception that occured.
     * @return The friendly error to send to the users in chat.
     */
    Object onParamViolation(ParamViolationException ex);

    /**
     * This may occur when a user attempts to perform a command
     * on a {@link Handler module} that has been disabled.
     *
     * @param ex The exception that occured.
     * @return The friendly error to send to users in chat.
     */
    Object onDisabled(ModuleDisabledException ex);

    /**
     * This will occur if an {@link Exception exception} occurs
     * when attempting to perform the command. This would normally due to a
     * module having an uncaught exception but is also a fall back in case
     * there is a bug in {@link Commandler}.
     *
     * @param ex The exception that occured.
     * @return The friendly error to send to users in chat.
     */
    <X extends Exception> Object onException(X ex);
}
