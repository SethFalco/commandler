package com.elypia.commandler.validation;

import com.elypia.commandler.events.CommandEvent;
import com.elypia.commandler.metadata.MetaParam;

import java.lang.annotation.Annotation;

public interface IParamValidator<T, Tt extends Annotation> {
    boolean validate(CommandEvent event, T t, Tt tt, MetaParam param);
    String help(Tt annotation);
}
