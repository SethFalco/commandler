package com.elypia.commandler.validation;

import com.elypia.commandler.interfaces.ICommandEvent;

import javax.validation.*;
import java.lang.annotation.Annotation;

public abstract class CommandConstraintValidator<E extends ICommandEvent, A extends Annotation, T> implements ConstraintValidator<A, T> {

    protected E event;

    public CommandConstraintValidator(E event) {
        this.event = event;
    }

    @Override
    public boolean isValid(T value, ConstraintValidatorContext context) {
        return isValid(event, value, context);
    }

    public abstract boolean isValid(E event, T value, ConstraintValidatorContext context);

    public E getEvent() {
        return event;
    }
}
