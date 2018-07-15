package com.elypia.commandler.impl;

import com.elypia.commandler.events.CommandEvent;
import com.elypia.commandler.metadata.MetaParam;

import java.lang.annotation.Annotation;

public interface IParamValidator<I, A extends Annotation> {
    boolean validate(CommandEvent event, I input, A annotation, MetaParam param);
    String help(A annotation);
}
