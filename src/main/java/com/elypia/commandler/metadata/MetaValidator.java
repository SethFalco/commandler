package com.elypia.commandler.metadata;

import com.elypia.commandler.annotations.validation.Validation;

import java.lang.annotation.Annotation;

public class MetaValidator {

    private Annotation annotation;
    private Class<? extends Annotation> clazz;
    private Validation validatorAnnotation;

    protected static MetaValidator of(Annotation annotation) {
        return new MetaValidator(annotation);
    }

    private MetaValidator(Annotation annotation) {
        this.annotation = annotation;
        clazz = annotation.annotationType();
        validatorAnnotation = clazz.getAnnotation(Validation.class);
    }

    public Annotation getAnnotation() {
        return annotation;
    }

    public Validation getValidatorAnnotation() {
        return validatorAnnotation;
    }

    public Class<? extends Annotation> getAnnotationType() {
        return clazz;
    }
}
