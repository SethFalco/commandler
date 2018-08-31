package com.elypia.commandler.impl;

import java.lang.annotation.Annotation;

/**
 * The ICommandValidator should be implemented on AnnotationValidators.
 * This should be used on commands where requirements must be met
 * else the command can not be performed, such as permissions or message
 * channel type. Custom validation can also be created for example
 * if an achievement system is in use.
 *
 * @param <CE> The command event that requires validation.
 * @param <T> The annotation that will trigger this validation.
 */
public interface ICommandValidator<CE extends ICommandEvent, T extends Annotation> {

    /**
     * Using the annotation, validate the event against the requirement.
     *
     * @param event The command event that required validation.
     * @param annotation The annotation and it's fields to validate against.
     * @return If this command has passed this validation.
     */
    boolean validate(CE event, T annotation);

    String help(T annotation);
}
