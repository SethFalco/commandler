package com.elypia.commandler.def;

import com.elypia.commandler.*;
import com.elypia.commandler.exceptions.misuse.*;
import com.elypia.commandler.interfaces.*;
import com.elypia.commandler.meta.data.*;
import org.slf4j.*;

import javax.validation.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A default implementation of {@link MisuseHandler}.
 */
public class DefMisuseHandler implements MisuseHandler {

    private static final Logger logger = LoggerFactory.getLogger(DefMisuseHandler.class);

    /**
     * Do nothing.
     *
     * @param ex
     * @return
     */
    @Override
    public String onOnlyPrefix(OnlyPrefixException ex) {
        return null;
    }

    /**
     * Do nothing.
     *
     * @param ex
     * @return
     */
    @Override
    public String onModuleNotFound(ModuleNotFoundException ex) {
        return null;
    }

    @Override
    public String onParamMismatch(ParamCountMismatchException ex) {
        Objects.requireNonNull(ex);
        Input input = ex.getInput();
        MetaCommand command = input.getCommand();
        String format =
            "Command failed; you provided the wrong number of parameters.\n" +
            "Module: %s\n" +
            "Command: %s\n" +
            "\n" +
            "Provided:\n" +
            "%s\n" +
            "\n" +
            "Possibilities:\n" +
            "%s";

        StringJoiner commandJoiner = new StringJoiner("\n");

        String moduleName = input.getModule().getName();
        String commandName = command.getName();
        return String.format(format, moduleName, commandName, input.toString(), commandJoiner.toString());
    }

    @Override
    public String onNoDefaultCommand(NoDefaultCommandException ex) {
        Objects.requireNonNull(ex);
        String format =
            "Command failed; this module has no default command.\n" +
            "Module: %s\n" +
            "\n" +
            "Possibilities:\n" +
            "%s\n" +
            "\n" +
            "See the help command for more information.";

        MetaModule module = ex.getModule();
        return String.format(format, module.getName(), module);
    }

    @Override
    public String onParamParse(ParamParseException ex) {
        Objects.requireNonNull(ex);
        Input input = ex.getInput();
        MetaParam param = ex.getParam();
        String format =
            "Command failed; I couldn't interpret '%s', as the parameter '%s' (%s).\n" +
            "Module: %s\n" +
            "Command: %s\n" +
            "\n" +
            "Required:\n" +
            "%s";

        String item = ex.getItem();
        String paramName = param.getName();
        String paramHelp = param.getHelp();
        String module = input.getModule().getName();
        MetaCommand command = input.getCommand();
        String commandName = command.getName();
        String paramString = command.toParamsString();

        return String.format(format, item, paramName, paramHelp, module, commandName, paramString);
    }

    @Override
    public String onListUnsupported(ListUnsupportedException ex) {
        Objects.requireNonNull(ex);
        Input input = ex.getInput();
        String format =
            "Command failed; the input, [%s], for parameter '%s' can't be a list.\n" +
            "Module: %s\n" +
            "Command: %s\n" +
            "\n" +
            "Required:\n" +
            "%s";

        StringJoiner joiner = new StringJoiner(", ");
        ex.getItems().forEach(item -> joiner.add("'" + item + "'"));

        String module = input.getModule().getName();
        MetaCommand command = input.getCommand();

        return String.format(format, joiner, ex.getParam().getName(), module, command.getName(), command.toParamsString());
    }

    @Override
    public String onParamViolation(ParamViolationException ex) {
        Objects.requireNonNull(ex);
        Set<ConstraintViolation<Handler>> violations = ex.getViolations();
        var commandViolations = violations.stream()
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

        return generateMessage(format, ex.getInput());
    }

    private String formatViolationString(String name, ConstraintViolation violation) {
        String message = violation.getMessage();
        message = message.substring(0, 1).toUpperCase() + message.substring(1);

        message = "\n" + name + ": " + message;

        if (message.charAt(message.length() - 1) != '.')
            message += ".";

        return message;
    }

    private String generateMessage(String format, Input input) {
        String module = input.getModule().getName();
        String command = input.getCommand().getName();

        return String.format(format, module, command);
    }

    @Override
    public String onDisabled(ModuleDisabledException ex) {
        String format =
            "Command failed; this module is currently disabled due to live issues.\n" +
            "Module: %s";

        return String.format(format, ex.getInput().getModule().getName());
    }

    @Override
    public <X extends Exception> String onException(X ex) {
        String text = "An unknown error occured.";
        logger.error(text, ex);
        return text;
    }
}






