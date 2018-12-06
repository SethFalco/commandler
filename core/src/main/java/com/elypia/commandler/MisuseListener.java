package com.elypia.commandler;

import com.elypia.commandler.annotations.Param;
import com.elypia.commandler.impl.*;
import com.elypia.commandler.interfaces.IMisuseListener;
import com.elypia.commandler.metadata.*;
import org.slf4j.*;

import javax.validation.ConstraintViolation;
import java.util.*;

/**
 * A default implementation of {@link IMisuseListener}.
 */
public class MisuseListener<C, E, M> implements IMisuseListener<C, E, M> {

    private static final Logger logger = LoggerFactory.getLogger(MisuseListener.class);

    @Override
    public Object onModuleNotFound(String content) {
        return null;
    }

    @Override
    public Object onParamCountMismatch(CommandInput input, CommandData commandData) {
        String format = "Command failed; you provided the wrong number of parameters.\nModule: %s\nCommand: %s\n\nProvided:\n%s\n\nPossibilities:\n%s";
        StringJoiner commandJoiner = new StringJoiner("\n");

        for (CommandData command : commandData.getOverloads(true))
            commandJoiner.add(command.toString());

        String commandName = commandData.getCommand().name();
        String moduleName = commandData.getModuleData().getAnnotation().name();
        return String.format(format, moduleName, commandName, input.toString(), commandJoiner.toString());
    }

    @Override
    public Object onDefaultNotFound(ICommandEvent event) {
        String format = "Command failed; this module has no default command.\nModule: %s\n\nPossibilities:\n%s\n\nSee the help command for more information.";
        ModuleData module = event.getInput().getModuleData();
        return String.format(format, module.getAnnotation().name(), module);
    }

    @Override
    public Object onParamParseFailure(ICommandEvent event, ParamData paramData, Class<?> type, String item) {
        String format = "Command failed; I couldn't interpret '%s', as the parameter '%s' (%s).\nModule: %s\nCommand: %s\n\nRequired:\n%s";

        CommandInput input = event.getInput();
        Param param = paramData.getAnnotation();
        String module = input.getModuleData().getAnnotation().name();
        CommandData command = input.getCommandData();

        return String.format(format, item, param.name(), param.help(), module, command.getCommand().name(), command);
    }

    @Override
    public Object onListNotSupported(ICommandEvent event, ParamData paramData, List<String> items) {
        String format = "Command failed; the input, [%s], for parameter '%s' can't be a list.\nModule: %s\nCommand: %s\n\nRequired:\n%s";

        StringJoiner joiner = new StringJoiner(", ");
        items.forEach(item -> joiner.add("'" + item + "'"));

        CommandInput input = event.getInput();
        Param param = paramData.getAnnotation();
        String module = input.getModuleData().getAnnotation().name();
        CommandData command = input.getCommandData();

        return String.format(format, joiner, param.name(), module, command.getCommand().name(), command);
    }

    @Override
    public Object onCommandInvalidated(ICommandEvent<C, E, M> event, CommandData commandData, ICommandValidator validator) {
        String format = "Command failed; the command was invalidated.\nModule: %s\nCommand: %s";
        return generateMessage(format, event);
    }

    @Override
    public <H extends IHandler<C, E, M>> Object onParamInvalidated(ICommandEvent<C, E, M> event, Set<ConstraintViolation<H>> violations) {
        String format = "Command failed; a parameter was invalidated.\nModule: %s\nCommand: %s";
        return generateMessage(format, event);
    }

    private Object generateMessage(String format, ICommandEvent<C, E, M> event) {
        CommandInput input = event.getInput();
        String module = input.getModuleData().getAnnotation().name();
        String command = input.getCommandData().getCommand().name();

        return String.format(format, module, command);
    }

    @Override
    public Object onModuleDisabled(ICommandEvent<C, E, M> event) {
        String format = "Command failed; this module is currently disabled due to live issues.\nModule: %s";
        return String.format(format, event.getInput().getModuleData().getAnnotation().name());
    }

    @Override
    public <X extends Exception> Object onException(X ex) {
        String text = "An unknown error occured.";
        logger.error(text, ex);
        return text;
    }
}
