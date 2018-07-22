package com.elypia.commandler;

import com.elypia.commandler.impl.IParamValidator;

import java.lang.annotation.Annotation;

public interface IJDAParamValidator<O, A extends Annotation> extends IParamValidator<JDACommand, O, A> {

}
