/*
 * Copyright 2019-2019 Elypia CIC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.elypia.commandler;

import org.elypia.commandler.api.MisuseHandler;
import org.elypia.commandler.event.ActionEvent;
import org.elypia.commandler.exceptions.misuse.*;
import org.elypia.commandler.metadata.*;
import org.slf4j.*;

import java.util.Objects;

/**
 * A default implementation of {@link MisuseHandler}.
 * You can use this to get started quickly and add more on top.
 *
 * It's recommend when configuring your own exceptions
 *
 * @author seth@elypia.org (Syed Shah)
 */
public class DefaultMisuseHandler implements MisuseHandler {

    private static final Logger logger = LoggerFactory.getLogger(DefaultMisuseHandler.class);

    @Override
    public <X extends MisuseException> Object handle(X ex) {
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
            return onMisuseException(ex);
    }

    /**
     * @param ex The exception that occured.
     * @return Always returns null.
     */
    private String onOnlyPrefix(OnlyPrefixException ex) {
        return null;
    }

    /**
     * @param ex The exception that occured.
     * @return Always returns null.
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
    private <X extends MisuseException> String onMisuseException(X ex) {
        String text = "An unknown error occured.";
        logger.error(text, ex);
        return text;
    }
}






