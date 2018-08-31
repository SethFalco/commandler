package com.elypia.commandler.jda;

import com.elypia.commandler.impl.IParamValidator;

import java.lang.annotation.Annotation;
import java.util.Objects;
import java.util.function.Function;

public abstract class IJDAParamValidator<O, A extends Annotation> extends IParamValidator<JDACommand, O, A> {

    public IJDAParamValidator(Function<A, String> help) {
        super(help);
    }
}
