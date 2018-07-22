package com.elypia.commandler.impl;

import java.lang.annotation.Annotation;

public interface ICommandValidator<CE extends ICommandEvent, T extends Annotation> {
    boolean validate(CE event, T annotation);
    String help(T annotation);
}
