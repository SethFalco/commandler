package com.elypia.commandler.exceptions.misuse;

import com.elypia.commandler.Input;
import com.elypia.commandler.exceptions.InputException;
import com.elypia.commandler.interfaces.Handler;

import javax.validation.ConstraintViolation;
import java.util.*;

public class ParamViolationException extends InputException {

    private Set<ConstraintViolation<Handler>> violations;

    public ParamViolationException(Input input, Set<ConstraintViolation<Handler>> violations) {
        super(input);
        this.violations = Objects.requireNonNull(violations);
    }

    public Set<ConstraintViolation<Handler>> getViolations() {
        return violations;
    }
}
