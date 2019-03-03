package com.elypia.commandler.impl;

import com.elypia.commandler.*;
import com.elypia.commandler.interfaces.*;
import com.elypia.commandler.metadata.data.*;
import org.slf4j.*;

import javax.validation.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A default implementation of {@link com.elypia.commandler.interfaces.MisuseListener}.
 */
public class DefaultMisuseListener<S, M> implements MisuseListener<S, M> {

    private static final Logger logger = LoggerFactory.getLogger(DefaultMisuseListener.class);

    @Override
    public String onModuleNotFound(String content) {
        return null;
    }

    @Override
    public String onParamCountMismatch(EventInput input) {
        String format = "Command failed; you provided the wrong number of parameters.\nModule: %s\nCommand: %s\n\nProvided:\n%s\n\nPossibilities:\n%s";
        StringJoiner commandJoiner = new StringJoiner("\n");

        CommandData command = input.getCommandData();
        List<ParamData> defaultParams = command.getDefaultParams();

//        commandler.
//
//        for (List<OverloadData> overload : command.getOverloads()) {
//            commandJoiner.add(overload.toString());
//        }

        String commandName = command.getName();
        String moduleName = input.getModuleData().getName();
        return String.format(format, moduleName, commandName, input.toString(), commandJoiner.toString());
    }

    @Override
    public String onDefaultNotFound(CommandlerEvent event) {
        String format = "Command failed; this module has no default command.\nModule: %s\n\nPossibilities:\n%s\n\nSee the value command for more information.";
        ModuleData module = event.getInput().getModuleData();
        return String.format(format, module.getName(), module);
    }

    @Override
    public String onParamParseFailure(CommandlerEvent event, ParamData param, Class<?> type, String item) {
        String format = "Command failed; I couldn't interpret '%s', as the parameter '%s' (%s).\nModule: %s\nCommand: %s\n\nRequired:\n%s";

        EventInput input = event.getInput();
        String module = input.getModuleData().getName();
        CommandData command = input.getCommandData();

        return String.format(format, item, param.getName(), param.getHelp(), module, command.getName(), command.toParamsString());
    }

    @Override
    public String onListNotSupported(CommandlerEvent event, ParamData param, List<String> items) {
        String format = "Command failed; the input, [%s], for parameter '%s' can't be a list.\nModule: %s\nCommand: %s\n\nRequired:\n%s";

        StringJoiner joiner = new StringJoiner(", ");
        items.forEach(item -> joiner.add("'" + item + "'"));

        EventInput input = event.getInput();
        String module = input.getModuleData().getName();
        CommandData command = input.getCommandData();

        return String.format(format, joiner, param.getName(), module, command.getName(), command.toParamsString());
    }

    @Override
    public <H extends Handler<S, M>> String onInvalidated(CommandlerEvent<S, M> event, Set<ConstraintViolation<H>> violations) {
        var commandViolations = violations.parallelStream()
            .filter((v) -> v.getInvalidValue() instanceof CommandlerEvent)
            .collect(Collectors.toList());

        var parameterViolations = new ArrayList<>(violations);
        parameterViolations.removeAll(commandViolations);

        StringJoiner invalidType = new StringJoiner(" ");

        if (!commandViolations.isEmpty())
            invalidType.add("the command");
        if (!parameterViolations.isEmpty())
            invalidType.add("a parameter");

        String format =
            "Command failed; " + invalidType.toString() + " was invalidated.\n" +
            "Module: %s\n" +
            "Command: %s\n";

        for (var violation : commandViolations)
            format += formatViolationString("command", violation);

        if (!commandViolations.isEmpty())
            format += "\n";

        for (var violation : parameterViolations) {
            Iterator<Path.Node> iter = violation.getPropertyPath().iterator();
            Path.Node last = null;

            while (iter.hasNext())
                last = iter.next();

            format += formatViolationString(last.getName(), violation);
        }

        return generateMessage(format, event);
    }

    private String formatViolationString(String name, ConstraintViolation violation) {
        String message = violation.getMessage();
        message = message.substring(0, 1).toUpperCase() + message.substring(1);

        message = "\n" + name + ": " + message;

        if (message.charAt(message.length() - 1) != '.')
            message += ".";

        return message;
    }

    private String generateMessage(String format, CommandlerEvent<S, M> event) {
        EventInput input = event.getInput();
        String module = input.getModuleData().getName();
        String command = input.getCommandData().getName();

        return String.format(format, module, command);
    }

    @Override
    public String onModuleDisabled(CommandlerEvent<S, M> event) {
        String format =
            "Command failed; this module is currently disabled due to live issues.\n" +
            "Module: %s";

        return String.format(format, event.getInput().getModuleData().getName());
    }

    @Override
    public <X extends Exception> String onException(X ex) {
        String text = "An unknown error occured.";
        logger.error(text, ex);
        return text;
    }
}
