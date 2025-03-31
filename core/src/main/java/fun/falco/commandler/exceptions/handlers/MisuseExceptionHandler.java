/*
 * Copyright 2019-2025 Seth Falco and Commandler Contributors
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

package fun.falco.commandler.exceptions.handlers;

import java.util.Objects;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.deltaspike.core.api.exception.control.ExceptionHandler;
import org.apache.deltaspike.core.api.exception.control.Handles;
import org.apache.deltaspike.core.api.exception.control.event.ExceptionEvent;
import fun.falco.commandler.event.ActionEvent;
import fun.falco.commandler.exceptions.misuse.AbstractMisuseException;
import fun.falco.commandler.exceptions.misuse.ListUnsupportedException;
import fun.falco.commandler.exceptions.misuse.ModuleDisabledException;
import fun.falco.commandler.exceptions.misuse.NoDefaultCommandException;
import fun.falco.commandler.exceptions.misuse.ParamCountMismatchException;
import fun.falco.commandler.exceptions.misuse.ParamParseException;
import fun.falco.commandler.i18n.CommandlerMessageResolver;
import fun.falco.commandler.metadata.MetaCommand;
import fun.falco.commandler.metadata.MetaController;
import fun.falco.commandler.metadata.MetaParam;
import fun.falco.commandler.producers.MessageSender;

/**
 * Default handling for certain {@link AbstractMisuseException} that occur
 * during runtime.
 *
 * <p>It's recommend when configuring your own exceptions to derive from this
 * class as a base.</p>
 *
 * @author seth@falco.fun (Seth Falco)
 */
@ApplicationScoped
@ExceptionHandler
public class MisuseExceptionHandler {

    private final CommandlerMessageResolver commandlerMessages;
    private final MessageSender sender;

    @Inject
    public MisuseExceptionHandler(CommandlerMessageResolver commandlerMessages, MessageSender sender) {
        this.commandlerMessages = commandlerMessages;
        this.sender = sender;
    }

    /**
     * @param ex Exception that occurred.
     */
    public void onParamMismatch(@Handles ExceptionEvent<ParamCountMismatchException> ex) {
        Objects.requireNonNull(ex);
        ActionEvent<?, ?> event = ex.getException().getActionEvent();
        MetaCommand metaCommand = event.getMetaCommand();
        String format =
            "Command failed: you provided the wrong amount of parameters.\n" +
            "Module: %s\n" +
            "Command: %s\n" +
            "Required: %s\n" +
            "Provided: %s";

        String moduleName = commandlerMessages.getMessage(event.getMetaController().getName());
        String commandName = commandlerMessages.getMessage(metaCommand.getName());
        sender.send(String.format(format, moduleName, commandName, metaCommand.toParamString(), event.getAction().toParamString()));
        ex.handled();
    }

    /**
     * @param ex Exception that occurred.
     */
    public void onNoDefaultCommand(@Handles ExceptionEvent<NoDefaultCommandException> ex) {
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
        sender.send(String.format(format, metaController.getName(), commands));
        ex.handled();
    }

    /**
     * @param ex Exception that occurred.
     */
    public void onParamParse(@Handles ExceptionEvent<ParamParseException> ex) {
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

        sender.send(String.format(
            format,
            ex.getException().getItem(),
            commandlerMessages.getMessage(metaParam.getName()),
            commandlerMessages.getMessage(input.getMetaController().getName()),
            commandlerMessages.getMessage(metaCommand.getName()),
            metaCommand.toParamString(),
            input.getAction().toParamString()
        ));
        ex.handled();
    }

    /**
     * @param ex Exception that occurred.
     */
    public void onListUnsupported(@Handles ExceptionEvent<ListUnsupportedException> ex) {
        Objects.requireNonNull(ex);
        String format =
            "Command failed; the parameter '%s' can't be a list.\n" +
            "Module: %s\n" +
            "Command: %s\n" +
            "Required: %s\n" +
            "Provided: %s";

        ActionEvent input = ex.getException().getActionEvent();
        String param = commandlerMessages.getMessage(ex.getException().getMetaParam().getName());
        String module = commandlerMessages.getMessage(input.getMetaController().getName());
        MetaCommand metaCommand = input.getMetaCommand();
        String commandName = commandlerMessages.getMessage(metaCommand.getName());
        String commandParams = metaCommand.toParamString();

        sender.send(String.format(format, param, module, commandName, commandParams, input.getAction().toParamString()));
        ex.handled();
    }

    /**
     * @param ex Exception that occurred.
     */
    public void onDisabled(@Handles ExceptionEvent<ModuleDisabledException> ex) {
        String format =
            "Command failed; this module is currently disabled due to live issues.\n" +
            "Module: %s";

        sender.send(String.format(format, commandlerMessages.getMessage(ex.getException().getActionEvent().getMetaController().getName())));
        ex.handled();
    }
}
