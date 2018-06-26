package com.elypia.commandler.annotations.validation;

import java.lang.annotation.*;

/**
 * All validators for Commandler should be annotated with the
 * {@link Validation} annotation so Commandler knows it is a validator.
 */

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Validation {

    /**
     * @return The path to the icon that represents this validator.
     */

    String value() default "";
}
