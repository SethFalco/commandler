package com.elypia.commandler.validation.annotations;

import java.lang.annotation.*;

/**
 * Limit the value that a number can be, by default the limits are
 * what a Java {@link Long} can handle.
 */

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Limit {
    long min() default Long.MIN_VALUE;
    long max() default Long.MAX_VALUE;
}
