package com.elypia.commandler.metadata;

import com.elypia.commandler.annotations.command.Param;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

public class MetaParam {

    private Parameter parameter;
    private Param annotation;

    public static MetaParam of(Parameter parameter, Param annotation) {
        return new MetaParam(parameter, annotation);
    }

    private MetaParam(Parameter parameter, Param annotation) {
        this.parameter = parameter;
        this.annotation = annotation;
    }

    public Annotation[] getAnnotations() {
        return parameter.getAnnotations();
    }

    public Parameter getParameter() {
        return parameter;
    }

    public Param getParams() {
        return annotation;
    }
}
