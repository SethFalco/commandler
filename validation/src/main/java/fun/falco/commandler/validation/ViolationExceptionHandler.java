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

package fun.falco.commandler.validation;

import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Path;

import org.apache.deltaspike.core.api.exception.control.ExceptionHandler;
import org.apache.deltaspike.core.api.exception.control.Handles;
import org.apache.deltaspike.core.api.exception.control.event.ExceptionEvent;

import fun.falco.commandler.i18n.CommandlerMessageResolver;
import fun.falco.commandler.producers.MessageSender;

/**
 * Handling the {@link ViolationException}, this can be overridden
 * by expressing an alternative of this class.
 *
 * @author seth@falco.fun (Seth Falco)
 */
@ApplicationScoped
@ExceptionHandler
public class ViolationExceptionHandler {

    private final CommandlerMessageResolver commandlerMessages;
    private final MessageSender sender;

    @Inject
    public ViolationExceptionHandler(CommandlerMessageResolver commandlerMessages, MessageSender sender) {
        this.commandlerMessages = commandlerMessages;
        this.sender = sender;
    }

    /**
     * @param event Exception that occurred.
     * @throws NullPointerException if exception is null.
     */
    public void onViolation(@Handles ExceptionEvent<ViolationException> event) {
        Objects.requireNonNull(event);
        ViolationException ex = event.getException();

        Set<ConstraintViolation<Object>> violations = ex.getViolations();

        StringBuilder format = new StringBuilder(
            "Command failed; the command was invalidated.\n" +
            "Module: %s\n" +
            "Command: %s\n"
        );

        for (var violation : violations) {
            Iterator<Path.Node> iter = violation.getPropertyPath().iterator();
            Path.Node last = null;

            while (iter.hasNext()) {
                last = iter.next();
            }

            Objects.requireNonNull(last);
            format.append(commandlerMessages.getMessage(last.getName())).append(": ").append(violation.getMessage());
        }

        String module = commandlerMessages.getMessage(ex.getActionEvent().getMetaController().getName());
        String command = commandlerMessages.getMessage(ex.getActionEvent().getMetaCommand().getName());
        String response = String.format(format.toString(), module, command);

        sender.send(response);
        event.handled();
    }
}
