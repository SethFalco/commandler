/*
 * Copyright 2019-2020 Elypia CIC
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

import org.apache.deltaspike.core.api.exception.control.*;
import org.apache.deltaspike.core.api.exception.control.event.ExceptionEvent;
import org.elypia.commandler.event.ActionEvent;
import org.elypia.commandler.exceptions.misuse.*;
import org.elypia.commandler.metadata.*;
import org.slf4j.*;

import javax.enterprise.context.ApplicationScoped;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * The default handling for certain {@link AbstractMisuseException}
 * that occur during runtime.
 *
 * It's recommend when configuring your own exceptions
 * to derive from this class as a base.
 *
 * @author seth@elypia.org (Seth Falco)
 */
@ApplicationScoped
@ExceptionHandler
public class DefaultMisuseHandler {

    private static final Logger logger = LoggerFactory.getLogger(DefaultMisuseHandler.class);

    /**
     * @param ex The exception that occured.
     * @return Always returns null.
     */
    public String onOnlyPrefix(@Handles ExceptionEvent<OnlyPrefixException> ex) {
        return null;
    }

    /**
     * @param ex The exception that occured.
     * @return Always returns null.
     */
    public String onModuleNotFound(@Handles ExceptionEvent<ModuleNotFoundException> ex) {
        return null;
    }

    /**
     * @param ex The exception that occured.
     * @return Don't send any messages.
     */
    public String onParamMismatch(@Handles ExceptionEvent<ParamCountMismatchException> ex) {
        Objects.requireNonNull(ex);
        ActionEvent<?, ?> event = ex.getException().getActionEvent();
        MetaCommand metaCommand = event.getMetaCommand();
        String format =
            "Command failed: you provided the wrong amount of parameters.\n" +
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
    public String onNoDefaultCommand(@Handles ExceptionEvent<NoDefaultCommandException> ex) {
        Objects.requireNonNull(ex);
        String format =
            "Command failed; this module has no default command.\n" +
            "Module: %s\n" +
            "\n" +
            "Possibilities:\n" +
            "%s\n" +
            "\n" +
            "See the help command for more information.";

        MetaController metaController = ex.getException().getModule();
        String commands = metaController.getPublicCommands().stream()
            .map(MetaCommand::toString)
            .collect(Collectors.joining("\n"));
        return String.format(format, metaController.getName(), commands);
    }

    /**
     * @param ex The exception that occured.
     * @return Don't send any messages.
     */
    public String onParamParse(@Handles ExceptionEvent<ParamParseException> ex) {
        Objects.requireNonNull(ex);
        String format =
            "Command failed; I couldn't interpret '%s', as the parameter '%s'.\n" +
            "Module: %s\n" +
            "Command: %s\n" +
            "Required: %s\n" +
            "Provided: %s";

        ActionEvent input = ex.getException().getActionEvent();
        MetaParam metaParam = ex.getException().getMetaParam();
        MetaCommand metaCommand = input.getMetaCommand();

        return String.format(
            format,
            ex.getException().getItem(),
            metaParam.getName(),
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
    public String onListUnsupported(@Handles ExceptionEvent<ListUnsupportedException> ex) {
        Objects.requireNonNull(ex);
        String format =
            "Command failed; the parameter '%s' can't be a list.\n" +
            "Module: %s\n" +
            "Command: %s\n" +
            "Required: %s\n" +
            "Provided: %s";

        ActionEvent input = ex.getException().getActionEvent();
        String param = ex.getException().getMetaParam().getName();
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
    public String onDisabled(@Handles ExceptionEvent<ModuleDisabledException> ex) {
        String format =
            "Command failed; this module is currently disabled due to live issues.\n" +
            "Module: %s";

        return String.format(format, ex.getException().getActionEvent().getMetaController().getName());
    }

    /**
     * @param ex The exception that occured.
     * @param <X> A type of exception.
     * @return Don't send any messages.
     */
    public <X extends AbstractMisuseException> String onMisuseException(@Handles ExceptionEvent<X> ex) {
        String text = "An unknown error occured.";
        logger.error(text, ex);
        return text;
    }
}
