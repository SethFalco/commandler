package com.elypia.commandler.validation.annotations;

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
    long min() default Long.MIN_VALUE;
    long max() default Long.MAX_VALUE;
}
