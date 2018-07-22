package com.elypia.commandler.impl;

import com.elypia.commandler.metadata.MetaParam;

import java.lang.annotation.Annotation;

public interface IParamValidator<CE extends ICommandEvent, O, A extends Annotation> {
    boolean validate(CE event, O output, A annotation, MetaParam param);
    String help(A annotation);
}
