package com.elypia.commandler.impl;

import com.elypia.commandler.*;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.metadata.*;

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
    default Object onNoModule() {
        return null; // ? By default we just won't do anything at all.
    }

    /**
     * This may occur when we've found the command the user wanted to perform
     * but the command and none of it's overloads allow that number of parameters.
     *
     * @return The friendly error to send to users in chat.
     */
    default Object onParameterCountMismatch(CommandInput<?, ?, ?> input, MetaCommand<?, ?, ?> metaCommand) {
        String format = "You specified the '%s' command in the '%s' module but the parameters weren't what I expected.\n\n%s";

        StringBuilder parameters = new StringBuilder("Provided:\n");

        List<List<String>> lists = input.getParameters();

        if (lists.isEmpty())
            parameters.append("(0) None");
        else {
            StringJoiner parameterJoiner = new StringJoiner(" | ");

            for (List<String> list : lists) {
                StringJoiner itemJoiner = new StringJoiner(", ");

                for (String string : list)
                    itemJoiner.add("'" + string + "'");

                parameterJoiner.add(itemJoiner.toString());
            }

            parameters.append("(").append(lists.size()).append(") ").append(parameterJoiner.toString());
        }

        parameters.append("\n\nPossibilities:\n");

        StringJoiner commandJoiner = new StringJoiner("\n");

        for (MetaCommand<?, ?, ?> command : metaCommand.getOverloads(true)) {
            List<MetaParam> params = command.getMetaParams();

            if (params.isEmpty())
                commandJoiner.add("(0) None");
            else {
                StringJoiner itemJoiner = new StringJoiner(" | ");

                for (MetaParam param : params) {
                    String name = param.getParamAnnotation().name();

                    if (param.isList())
                        itemJoiner.add("['" + name + "']");
                    else
                        itemJoiner.add("'" + name + "'");
                }

                commandJoiner.add("(" + params.size() + ") " + itemJoiner.toString());
            }
        }

        parameters.append(commandJoiner.toString());

        String commandName = metaCommand.getCommand().name();
        String moduleName = metaCommand.getMetaModule().getModule().name();
        return String.format(format, commandName, moduleName, parameters.toString());
    }

    /**
     * This occurs when it looks like the user had attempted to perform a
     * {@link Default} command however the {@link Module} they specified doesn't
     * actually own a {@link Default} command.
     *
     * @return The friendly error to send to users in chat.
     */
    default Object onNoDefault(CommandEvent event) {
        String format = "You specified the '%s' module without a valid command, however this module has no default command.\n\nPossibilities:\n%s\n\nSee the help command for more information.";
        MetaModule<?, ?, ?> module = event.getInput().getMetaModule();

        StringJoiner commandJoiner = new StringJoiner("\n");

        for (MetaCommand<?, ?, ?> command : module.getMetaCommands()) {
            StringJoiner aliasJoiner = new StringJoiner(", ");

            for (String alias : command.getAliases())
                aliasJoiner.add("'" + alias + "'");

            commandJoiner.add("(" + command.getCommand().name() + ") " + aliasJoiner.toString());
        }

        return String.format(format, module.getModule().name(), commandJoiner.toString());
    }

    /**
     * This may occur whenever user input fails to parse.
     *
     * @param event The event that caused this failure.
     * @param type The data type we were required.
     * @param input The input the user provided.
     * @return The friendly error to send to users in chat.
     */
    default Object onFailedParameterParse(CommandEvent event, Class<?> type, String input) {
        String format = "You specified the '%s' command in the '%s' module, however I couldn't process your parameters.\n\n%s";
        return format;
    }

    /**
     * This may occur whenever the user has specified list
     * parameters however a list is not acceptable here.
     *
     * @param event
     * @param param
     * @param items
     * @return
     */
    default Object onSupportedList(CommandEvent event, MetaParam param, List<String> items) {
        return null;
    }

    /**
     * This occurs when an {@link ICommandValidator} invalidates the event.
     *
     * @return
     */
    default Object onCommandInvalidated() {
        return null;
    }

    /**
     * This occurs when an {@link IParamValidator} invalidates a parameter on the command.
     *
     * @return
     */
    default Object onParameterInvalidated() {
        return null;
    }

    /**
     * This may occur when a user attempts to perform a command
     * which isn't the {@link IHandler#help(CommandEvent)} command on
     * a {@link Module} that has been disabled.
     *
     * @param event The event that caused this failure.
     * @return The friendly error to send to users in chat.
     */
    default Object onModuleDisabled(CommandEvent event) {
        return null;
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
        return "An unknown error occured.";
    }
}
