package com.elypia.commandler.annotations.validation.param;

import com.elypia.commandler.annotations.validation.Validation;

import java.lang.annotation.*;

/**
 * Limit the value that a number can be, by default the limits are
 * what a Java {@link Long} can handle.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Validation
public @interface Limit {

    /**
     * The default minimum value this number can have.
     */
    long DEFAULT_MIN = Long.MIN_VALUE;

    /**
     * The default maximium value this number can have.
     */
    long DEFAULT_MAX = Long.MAX_VALUE;

    /**
     * @return The minimum value that the parameter can be, inclusive.
     */
    long min() default DEFAULT_MIN;

    /**
     * @return The maximum value that the parameter can be, exclusive.
     */
    long max() default DEFAULT_MAX;
}
