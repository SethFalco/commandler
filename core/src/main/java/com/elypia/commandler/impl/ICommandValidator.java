package com.elypia.commandler.impl;

import com.elypia.commandler.CommandEvent;

import java.lang.annotation.Annotation;

public interface ICommandValidator<T extends Annotation> {
    boolean validate(CommandEvent event, T annotation);
    String help(T annotation);
}
