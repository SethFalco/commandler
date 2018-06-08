package com.elypia.commandler.validation;

import com.elypia.commandler.events.MessageEvent;

import java.lang.annotation.Annotation;

public interface ICommandValidator<T extends Annotation> {
    void validate(MessageEvent event, T annotation) throws IllegalAccessException;
    String help(T annotation);
}
