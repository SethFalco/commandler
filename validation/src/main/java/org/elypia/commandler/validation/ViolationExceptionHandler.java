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

package org.elypia.commandler.validation;

import org.apache.deltaspike.core.api.exception.control.*;
import org.apache.deltaspike.core.api.exception.control.event.ExceptionEvent;
import org.elypia.commandler.api.Controller;
import org.elypia.commandler.event.ActionEvent;
import org.elypia.commandler.i18n.CommandlerMessageResolver;
import org.elypia.commandler.producers.MessageSender;
import org.slf4j.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.*;
import java.util.*;

/**
 * Handling the {@link ViolationException}, this can be overridden
 * by expressing an alternative of this class.
 *
 * @author seth@elypia.org (Seth Falco)
 */
@ApplicationScoped
@ExceptionHandler
public class ViolationExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ViolationExceptionHandler.class);

    private final CommandlerMessageResolver commandlerMessages;
    private final MessageSender sender;

    @Inject
    public ViolationExceptionHandler(CommandlerMessageResolver commandlerMessages, MessageSender sender) {
        this.commandlerMessages = commandlerMessages;
        this.sender = sender;
    }

    /**
     * @param event The exception that occured.
     * @throws NullPointerException if exception is null.
     */
    public void onViolation(@Handles ExceptionEvent<ViolationException> event) {
        Objects.requireNonNull(event);
        ViolationException ex = event.getException();

        List<ConstraintViolation<Controller>> commandViolations = new ArrayList<>();
        List<ConstraintViolation<Controller>> paramViolations = new ArrayList<>();

        for (ConstraintViolation<Controller> violation : ex.getViolations()) {
            // TODO: This is invalid now,
            if (violation.getInvalidValue() instanceof ActionEvent)
                commandViolations.add(violation);
            else
                paramViolations.add(violation);
        }

        StringJoiner invalidType = new StringJoiner(" ");

        if (!commandViolations.isEmpty())
            invalidType.add("the command");
        if (!paramViolations.isEmpty())
            invalidType.add("a parameter");

        StringBuilder format = new StringBuilder(
            "Command failed; " + invalidType.toString() + " was invalidated.\n" +
            "Module: %s\n" +
            "Command: %s\n"
        );

        for (var violation : commandViolations)
            format.append("command: ").append(violation.getMessage());

        if (!commandViolations.isEmpty())
            format.append("\n");

        for (var violation : paramViolations) {
            Iterator<Path.Node> iter = violation.getPropertyPath().iterator();
            Path.Node last = null;

            while (iter.hasNext())
                last = iter.next();

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
