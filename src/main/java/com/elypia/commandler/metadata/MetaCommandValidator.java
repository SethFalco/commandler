package com.elypia.commandler.metadata;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.annotations.Validator;

import java.lang.annotation.Annotation;

public class MetaCommandValidator {

    private Commandler commandler;
    private Annotation annotation;
    private Validator validatorAnnotation;
    private Class<? extends Annotation> clazz;

    public static MetaCommandValidator of(MetaCommand metaCommand, Annotation annotation) {
        return new MetaCommandValidator(metaCommand, annotation);
    }

    private MetaCommandValidator(MetaCommand metaCommand, Annotation annotation) {
        this.annotation = annotation;
        clazz = annotation.annotationType();
        validatorAnnotation = clazz.getAnnotation(Validator.class);

        commandler = metaCommand.getCommandler();
    }

    public Commandler getCommandler() {
        return commandler;
    }

    public Annotation getAnnotation() {
        return annotation;
    }

    public Validator getValidatorAnnotation() {
        return validatorAnnotation;
    }

    public Class<? extends Annotation> getAnnotationType() {
        return clazz;
    }
}
