package com.elypia.commandler.misuse;

import com.elypia.commandler.*;
import com.elypia.commandler.exceptions.*;
import com.elypia.commandler.interfaces.*;
import com.elypia.commandler.metadata.*;
import org.slf4j.*;

import javax.validation.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A default implementation of {@link MisuseHandler}.
 */
public class CommandMisuseListener implements MisuseHandler {

    private static final Logger logger = LoggerFactory.getLogger(CommandMisuseListener.class);

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
            "Command failed: you provided the wrong number of parameters.\n" +
            "Module: %s\n" +
            "Command: %s\n" +
            "Required: %s\n" +
            "Provided: %s";

        String moduleName = input.getModule().getName();
        String commandName = command.getName();
        return String.format(format, moduleName, commandName, command.toParamString(), input.toParamString());
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
        String format =
            "Command failed; I couldn't interpret '%s', as the parameter '%s' (%s).\n" +
            "Module: %s\n" +
            "Command: %s\n" +
            "Required: %s\n" +
            "Provided: %s";

        Input input = ex.getInput();
        MetaParam param = ex.getParam();
        MetaCommand command = input.getCommand();

        return String.format(
            format,
            ex.getItem(),
            param.getName(),
            param.getHelp(),
            input.getModule().getName(),
            command.getName(),
            command.toParamString(),
            input.toParamString()
        );
    }

    @Override
    public String onListUnsupported(ListUnsupportedException ex) {
        Objects.requireNonNull(ex);
        String format =
            "Command failed; the parameter '%s' can't be a list.\n" +
            "Module: %s\n" +
            "Command: %s\n" +
            "Required: %s\n" +
            "Provided: %s";

        Input input = ex.getInput();
        String param = ex.getParam().getName();
        String module = input.getModule().getName();
        MetaCommand command = input.getCommand();
        String commandName = command.getName();
        String commandParams = command.toParamString();

        return String.format(format, param, module, commandName, commandParams, input.toParamString());
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
            format += "command: " + violation.getMessage();

        if (!commandViolations.isEmpty())
            format += "\n";

        for (var violation : parameterViolations) {
            Iterator<Path.Node> iter = violation.getPropertyPath().iterator();
            Path.Node last = null;

            while (iter.hasNext())
                last = iter.next();

            format += last.getName() + ": " + violation.getMessage();
        }

        return generateMessage(format, ex.getInput());
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






