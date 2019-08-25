package com.elypia.commandler;

import com.elypia.commandler.api.ExceptionHandler;
import com.elypia.commandler.event.ActionEvent;
import com.elypia.commandler.exceptions.*;
import com.elypia.commandler.metadata.*;
import org.slf4j.*;

import java.util.Objects;

/**
 * A default implementation of {@link ExceptionHandler}.
 * You can use this to get started quickly and add more on top.
 *
 * It's recommend when configuring your own exceptions
 */
public class DefaultExceptionHandler implements ExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(DefaultExceptionHandler.class);

    @Override
    public <X extends Exception> Object handle(X ex) {
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
        else if (ex instanceof ModuleDisabledException)
            return onDisabled((ModuleDisabledException)ex);
        else
            return onException(ex);
    }

    /**
     * @param ex The exception that occured.
     * @return Don't send any messages.
     */
    private String onOnlyPrefix(OnlyPrefixException ex) {
        return null;
    }

    /**
     * @param ex The exception that occured.
     * @return Don't send any messages.
     */
    private String onModuleNotFound(ModuleNotFoundException ex) {
        return null;
    }

    /**
     * @param ex The exception that occured.
     * @return Don't send any messages.
     */
    private String onParamMismatch(ParamCountMismatchException ex) {
        Objects.requireNonNull(ex);
        ActionEvent<?, ?> event = ex.getActionEvent();
        MetaCommand metaCommand = event.getMetaCommand();
        String format =
            "Command failed: you provided the wrong number of parameters.\n" +
            "Module: %s\n" +
            "Command: %s\n" +
            "Required: %s\n" +
            "Provided: %s";

        String moduleName = event.getMetaController().getName();
        String commandName = metaCommand.getName();
        return String.format(format, moduleName, commandName, metaCommand.toParamString(), event.getAction().toParamString());
    }

    /**
     * @param ex The exception that occured.
     * @return Don't send any messages.
     */
    private String onNoDefaultCommand(NoDefaultCommandException ex) {
        Objects.requireNonNull(ex);
        String format =
            "Command failed; this module has no default command.\n" +
            "Module: %s\n" +
            "\n" +
            "Possibilities:\n" +
            "%s\n" +
            "\n" +
            "See the help command for more information.";

        MetaController module = ex.getModule();
        return String.format(format, module.getName(), module);
    }

    /**
     * @param ex The exception that occured.
     * @return Don't send any messages.
     */
    private String onParamParse(ParamParseException ex) {
        Objects.requireNonNull(ex);
        String format =
            "Command failed; I couldn't interpret '%s', as the parameter '%s' (%s).\n" +
            "Module: %s\n" +
            "Command: %s\n" +
            "Required: %s\n" +
            "Provided: %s";

        ActionEvent input = ex.getActionEvent();
        MetaParam metaParam = ex.getMetaParam();
        MetaCommand metaCommand = input.getMetaCommand();

        return String.format(
            format,
            ex.getItem(),
            metaParam.getName(),
            metaParam.getDescription(),
            input.getMetaController().getName(),
            metaCommand.getName(),
            metaCommand.toParamString(),
            input.getAction().toParamString()
        );
    }

    /**
     * @param ex The exception that occured.
     * @return Don't send any messages.
     */
    private String onListUnsupported(ListUnsupportedException ex) {
        Objects.requireNonNull(ex);
        String format =
            "Command failed; the parameter '%s' can't be a list.\n" +
            "Module: %s\n" +
            "Command: %s\n" +
            "Required: %s\n" +
            "Provided: %s";

        ActionEvent input = ex.getActionEvent();
        String param = ex.getMetaParam().getName();
        String module = input.getMetaController().getName();
        MetaCommand metaCommand = input.getMetaCommand();
        String commandName = metaCommand.getName();
        String commandParams = metaCommand.toParamString();

        return String.format(format, param, module, commandName, commandParams, input.getAction().toParamString());
    }

    /**
     * @param ex The exception that occured.
     * @return Don't send any messages.
     */
    private String onDisabled(ModuleDisabledException ex) {
        String format =
            "Command failed; this module is currently disabled due to live issues.\n" +
            "Module: %s";

        return String.format(format, ex.getActionEvent().getMetaController().getName());
    }

    /**
     * @param ex The exception that occured.
     * @return Don't send any messages.
     */
    private <X extends Exception> String onException(X ex) {
        String text = "An unknown error occured.";
        logger.error(text, ex);
        return text;
    }
}






