package com.elypia.commandler.annotations.validation.param;

import com.elypia.commandler.annotations.Validation;

import java.lang.annotation.*;

/**
 * Limit the value that a number can be, by default the limits are
 * what a Java {@link Long} can handle.
 */

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Validation("./resources/params/limit.svg")
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
