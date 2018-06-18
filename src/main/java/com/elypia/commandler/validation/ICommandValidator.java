package com.elypia.commandler.validation;

import com.elypia.commandler.events.MessageEvent;

import java.lang.annotation.Annotation;

public interface ICommandValidator<T extends Annotation> {
    boolean validate(MessageEvent event, T annotation);
    String help(T annotation);
}
