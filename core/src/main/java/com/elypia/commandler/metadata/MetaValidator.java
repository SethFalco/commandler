package com.elypia.commandler.metadata;

import com.elypia.commandler.annotations.validation.Validation;

import java.lang.annotation.Annotation;

public class MetaValidator {

    private Annotation annotation;
    private Class<? extends Annotation> type;
    private Validation validatorAnnotation;

    MetaValidator(Annotation annotation) {
        this.annotation = annotation;
        type = annotation.annotationType();
        validatorAnnotation = type.getAnnotation(Validation.class);
    }

    public Annotation getValidator() {
        return annotation;
    }

    public Validation getValidation() {
        return validatorAnnotation;
    }

    public Class<? extends Annotation> getType() {
        return type;
    }
}
