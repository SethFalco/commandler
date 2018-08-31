package com.elypia.commandler.jda;

import com.elypia.commandler.impl.ICommandValidator;

import java.lang.annotation.Annotation;

public interface IJDACommandValidator<O extends Annotation> extends ICommandValidator<JDACommand, O> {

}
