package com.elypia.commandler.annotations.validation;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Limit the value that a number can be, by default the limits are
 * what a Java {@link Long} can handle.
 */

@Target(PARAMETER)
@Retention(RUNTIME)
@Inherited
public @interface Limit {

    /**
     * @return The minimum value that the parameter can be, inclusive.
     */

    long min() default Long.MIN_VALUE;

    /**
     * @return The maximum value that the parameter can be, inclusive.
     */

    long max() default Long.MAX_VALUE;
}
