package com.elypia.commandler.exceptions.misuse;

import com.elypia.commandler.*;

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
