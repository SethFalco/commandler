package com.elypia.commandler.impl;

import com.elypia.commandler.*;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.metadata.*;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Whenever user errors occur such as using the bot incorrectly (<strong>not exceptions</strong>)
 * these methods are called to provide a friendly error message to the user.
 */
public interface IMisuseListener {

    /**
     * This will occur when the user attempts to do a command
     * where the root alias (first argument) doesn't match any of our
     * registered modules or static commands what so ever.
     *
     * @return The friendly error to send to the user.
     */
    default Object onNoModule(String content) {
        return null; // ? By default we just won't do anything at all.
    }

    /**
     * This may occur when we've found the command the user wanted to perform
     * but the command and none of it's overloads allow that number of parameters.
     *
     * @return The friendly error to send to users in chat.
     */
    default Object onParameterCountMismatch(CommandInput input, MetaCommand<?, ?, ?> metaCommand) {
        String format = "Command failed; you provided the wrong number of parameters.\nModule: %s\nCommand: %s\n\nProvided:\n%s\n\nPossibilities:\n%s";
        StringJoiner commandJoiner = new StringJoiner("\n");

        for (MetaCommand command : metaCommand.getOverloads(true))
            commandJoiner.add(command.toString());

        String commandName = metaCommand.getCommand().name();
        String moduleName = metaCommand.getMetaModule().getModule().name();
        return String.format(format, moduleName, commandName, input.toString(), commandJoiner.toString());
    }

    /**
     * This occurs when it looks like the user had attempted to perform a
     * {@link Default} command however the {@link Module} they specified doesn't
     * actually own a {@link Default} command.
     *
     * @return The friendly error to send to users in chat.
     */
    default Object onNoDefault(ICommandEvent event) {
        String format = "Command failed; this module has no default command.\nModule: %s\n\nPossibilities:\n%s\n\nSee the help command for more information.";
        MetaModule<?, ?, ?> module = event.getInput().getMetaModule();
        return String.format(format, module.getModule().name(), module);
    }

    /**
     * This may occur whenever user input fails to parse.
     *
     * @param event The event that caused this failure.
     * @param type The data type we were required.
     * @param item The input the user provided.
     * @return The friendly error to send to users in chat.
     */
    default Object onParseFailure(ICommandEvent event, MetaParam metaParam, Class<?> type, String item) {
        String format = "Command failed; I couldn't interpret '%s', as the parameter '%s' (%s).\nModule: %s\nCommand: %s\n\nRequired:\n%s";

        CommandInput input = event.getInput();
        Param param = metaParam.getParamAnnotation();
        String module = input.getMetaModule().getModule().name();
        MetaCommand<?, ?, ?> command = input.getMetaCommand();

        return String.format(format, item, param.name(), param.help(), module, command.getCommand().name(), command);
    }

    /**
     * This may occur whenever the user has specified list
     * parameters however a list is not acceptable here.
     *
     * @param event
     * @param metaParam
     * @param items
     * @return
     */
    default Object onUnsupportedList(ICommandEvent event, MetaParam metaParam, List<String> items) {
        String format = "Command failed; the input, [%s], for parameter '%s' can't be a list.\nModule: %s\nCommand: %s\n\nRequired:\n%s";

        StringJoiner joiner = new StringJoiner(", ");
        items.forEach(item -> joiner.add("'" + item + "'"));

        CommandInput input = event.getInput();
        Param param = metaParam.getParamAnnotation();
        String module = input.getMetaModule().getModule().name();
        MetaCommand<?, ?, ?> command = input.getMetaCommand();
        return String.format(format, joiner, param.name(), module, command.getCommand().name(), command);
    }

    /**
     * This occurs when an {@link ICommandValidator} invalidates the event.
     *
     * @return
     */ // ? Temp
    default Object onCommandInvalidated(ICommandEvent event, MetaCommand metaCommand, ICommandValidator validator) {
        String format = "Command failed; the command was invalidated.\nModule: %s\nCommand: %s";

        CommandInput input = event.getInput();
        String module = input.getMetaModule().getModule().name();
        String command = input.getMetaCommand().getCommand().name();

        return String.format(format, module, command);
    }

    /**
     * This occurs when an {@link IParamValidator} invalidates a parameter on the command.
     *
     * @return
     */ // ? Temp
    default Object onParameterInvalidated(ICommandEvent event, MetaCommand metaCommand, IParamValidator validator) {
        String format = "Command failed; a parameter was invalidated.\nModule: %s\nCommand: %s";

        CommandInput input = event.getInput();
        String module = input.getMetaModule().getModule().name();
        String command = input.getMetaCommand().getCommand().name();

        return String.format(format, module, command);
    }

    /**
     * This may occur when a user attempts to perform a command
     * which isn't the {@link IHandler#help(ICommandEvent)} command on
     * a {@link Module} that has been disabled.
     *
     * @param event The event that caused this failure.
     * @return The friendly error to send to users in chat.
     */
    default Object onModuleDisabled(ICommandEvent event) {
        String format = "Command failed; this module is currently disabled due to live issues.\nModule: %s";
        return String.format(format, event.getInput().getMetaModule().getModule().name());
    }

    /**
     * This will occur if an {@link Exception exception} occurs
     * when attempting to perform the command. This would normally due to a
     * module having an uncaught exception but is also a fall back in case
     * there is a bug in {@link Commandler}.
     *
     * @param ex The exception that occured.
     * @return The friendly error to send to users in chat.
     */ // ? It's a bad idea to actually show the exception to users as it may contain sensitive information.
    default Object onExceptionFailure(Exception ex) {
        LoggerFactory.getLogger(IMisuseListener.class).error("An exception occured during command processing.", ex);
        return "An unknown error occured.";
    }
}
