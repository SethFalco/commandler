package com.elypia.commandler.interfaces;

import com.elypia.commandler.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.exceptions.misuse.*;

import javax.validation.executable.ExecutableValidator;

/**
 * Whenever user errors occur such as using the bot incorrectly (<strong>not exceptions</strong>)
 * these methods are called to provide a friendly error message to the user.
 *
 * The return types are {@link Object} as we will use the {@link Provider}
 * internally to generate whatever should be sent.
 */
public interface MisuseHandler {

    default <T extends Exception> void route(T ex) {
        if (ex instanceof OnlyPrefixException)
            onOnlyPrefix((OnlyPrefixException)ex);
        else if (ex instanceof ModuleNotFoundException)
            onModuleNotFound((ModuleNotFoundException)ex);
        else if (ex instanceof ParamCountMismatchException)
            onParamMismatch((ParamCountMismatchException)ex);
        else if (ex instanceof NoDefaultCommandException)
            onNoDefaultCommand((NoDefaultCommandException)ex);
        else if (ex instanceof ParamParseException)
            onParamParse((ParamParseException)ex);
        else if (ex instanceof ListUnsupportedException)
            onListUnsupported((ListUnsupportedException)ex);
        else if (ex instanceof ParamViolationException)
            onParamViolation((ParamViolationException)ex);
        else if (ex instanceof ModuleDisabledException)
            onDisabled((ModuleDisabledException)ex);
        else
            onException(ex);
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
     * which isn't the {@link Handler#help(CommandlerEvent)} command on
     * a {@link Module} that has been disabled.
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
