package com.elypia.commandler.impl;

import com.elypia.commandler.CommandEvent;
import com.elypia.commandler.metadata.MetaParam;

import java.lang.annotation.Annotation;

public interface IParamValidator<O, A extends Annotation> {
    boolean validate(CommandEvent event, O output, A annotation, MetaParam param);
    String help(A annotation);
}
