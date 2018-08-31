package com.elypia.commandler.metadata;

import java.lang.annotation.Annotation;

public class MetaValidator {

    private Annotation annotation;
    private Class<? extends Annotation> type;

    MetaValidator(Annotation annotation) {
        this.annotation = annotation;
        type = annotation.annotationType();
    }

    public Annotation getValidator() {
        return annotation;
    }

    public Class<? extends Annotation> getType() {
        return type;
    }
}
