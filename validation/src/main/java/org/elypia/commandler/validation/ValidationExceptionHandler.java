package org.elypia.commandler.validation;

import org.elypia.commandler.api.*;
import org.elypia.commandler.event.ActionEvent;

import javax.validation.*;
import java.util.*;
import java.util.stream.Collectors;

public class ValidationExceptionHandler implements ExceptionHandler {

    @Override
    public <X extends Exception> Object handle(X ex) {
        if (ex instanceof ViolationException)
            return onViolation((ViolationException)ex);

        return null;
    }

    /**
     * @param ex The exception that occured.
     * @return An error String reporting all violations.
     */
    private String onViolation(ViolationException ex) {
        Objects.requireNonNull(ex);
        Set<ConstraintViolation<Controller>> violations = ex.getViolations();
        var commandViolations = violations.stream()
            .filter((v) -> v.getInvalidValue() instanceof ActionEvent)
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

        String module = ex.getActionEvent().getMetaController().getName();
        String command = ex.getActionEvent().getMetaCommand().getName();

        return String.format(format, module, command);
    }
}
