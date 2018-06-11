package com.elypia.commandler.annotations.validation.param;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Limit a String parameter to a certain set of values.
 */

@Target(PARAMETER)
@Retention(RUNTIME)
public @interface Option {

    /**
     * @return The only values this paremeter may be.
     * The check is non-case sensitive.
     */

    String[] value();
}
