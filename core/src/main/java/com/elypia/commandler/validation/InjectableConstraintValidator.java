package com.elypia.commandler.validation;

import javax.validation.*;
import java.lang.annotation.Annotation;

public abstract class InjectableConstraintValidator<A extends Annotation, T> implements ConstraintValidator<A, T> {

    @Override
    public boolean isValid(T value, ConstraintValidatorContext context) {}
}
