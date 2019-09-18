package org.elypia.commandler.validation;

import org.elypia.commandler.api.Controller;
import org.elypia.commandler.event.ActionEvent;
import org.elypia.commandler.exceptions.ActionException;

import javax.validation.ConstraintViolation;
import java.util.*;

public class ViolationException extends ActionException {

    private Set<ConstraintViolation<Controller>> violations;

    public ViolationException(ActionEvent<?, ?> action, Set<ConstraintViolation<Controller>> violations) {
        super(action);
        this.violations = Objects.requireNonNull(violations);
    }

    public Set<ConstraintViolation<Controller>> getViolations() {
        return violations;
    }
}
