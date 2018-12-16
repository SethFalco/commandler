package com.elypia.commandler;

import com.elypia.commandler.annotations.Param;
import com.elypia.commandler.interfaces.*;
import com.elypia.commandler.metadata.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.*;

import javax.validation.*;
import java.util.*;

/**
 * A default implementation of {@link IMisuseHandler}.
 */
public class MisuseHandler<S, M> implements IMisuseHandler<S, M> {

    private static final Logger logger = LoggerFactory.getLogger(MisuseHandler.class);

    @Override
    public String onModuleNotFound(String content) {
        return null;
    }

    @Override
    public String onParamCountMismatch(CommandInput input, CommandData commandData) {
        String format = "Command failed; you provided the wrong number of parameters.\nModule: %s\nCommand: %s\n\nProvided:\n%s\n\nPossibilities:\n%s";
        StringJoiner commandJoiner = new StringJoiner("\n");

        for (CommandData command : commandData.getOverloads())
            commandJoiner.add(command.toString());

        String commandName = commandData.getAnnotation().id();
        String moduleName = commandData.getModuleData().getAnnotation().id();
        return String.format(format, moduleName, commandName, input.toString(), commandJoiner.toString());
    }

    @Override
    public String onDefaultNotFound(ICommandEvent event) {
        String format = "Command failed; this module has no default command.\nModule: %s\n\nPossibilities:\n%s\n\nSee the help command for more information.";
        ModuleData module = event.getInput().getModuleData();
        return String.format(format, module.getAnnotation().id(), module);
    }

    @Override
    public String onParamParseFailure(ICommandEvent event, ParamData paramData, Class<?> type, String item) {
        String format = "Command failed; I couldn't interpret '%s', as the parameter '%s' (%s).\nModule: %s\nCommand: %s\n\nRequired:\n%s";

        CommandInput input = event.getInput();
        Param param = paramData.getAnnotation();
        String module = input.getModuleData().getAnnotation().id();
        CommandData command = input.getCommandData();

        return String.format(format, item, param.id(), param.help(), module, command.getAnnotation().id(), command);
    }

    @Override
    public String onListNotSupported(ICommandEvent event, ParamData paramData, List<String> items) {
        String format = "Command failed; the input, [%s], for parameter '%s' can't be a list.\nModule: %s\nCommand: %s\n\nRequired:\n%s";

        StringJoiner joiner = new StringJoiner(", ");
        items.forEach(item -> joiner.add("'" + item + "'"));

        CommandInput input = event.getInput();
        Param param = paramData.getAnnotation();
        String module = input.getModuleData().getAnnotation().id();
        CommandData command = input.getCommandData();

        return String.format(format, joiner, param.id(), module, command.getAnnotation().id(), command);
    }

    @Override
    public <H extends Handler<S, M>> String onInvalidated(ICommandEvent<S, M> event, Set<ConstraintViolation<H>> violations) {
        String format =
            "Command failed; a parameter was invalidated.\n" +
            "Module: %s\n" +
            "Command: %s\n";

        for (var violation : violations) {
            String message = StringUtils.capitalize(violation.getMessage());

            if (message.indexOf(message.length() - 1) != '.')
                message += ".";

            Path.Node violatedParam = CommandlerUtils.getLastElement(violation.getPropertyPath().iterator());
            message = violatedParam.getName() + ": " + message;

            format += "\n" + message + " (" + violation.getInvalidValue() + ")";
        }

        return generateMessage(format, event);
    }

    private String generateMessage(String format, ICommandEvent<S, M> event) {
        CommandInput input = event.getInput();
        String module = input.getModuleData().getAnnotation().id();
        String command = input.getCommandData().getAnnotation().id();

        return String.format(format, module, command);
    }

    @Override
    public String onModuleDisabled(ICommandEvent<S, M> event) {
        String format =
            "Command failed; this module is currently disabled due to live issues.\n" +
            "Module: %s";

        return String.format(format, event.getInput().getModuleData().getAnnotation().id());
    }

    @Override
    public <X extends Exception> String onException(X ex) {
        String text = "An unknown error occured.";
        logger.error(text, ex);
        return text;
    }
}
