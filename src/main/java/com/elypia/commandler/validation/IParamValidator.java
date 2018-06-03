package com.elypia.commandler.validation;

import com.elypia.commandler.metadata.MetaParam;

import java.lang.annotation.Annotation;

public interface IParamValidator<T, Tt extends Annotation> {
    void validate(T t, Tt tt, MetaParam param) throws IllegalAccessException;
}
