package com.elypia.commandler.annotations.validation.param;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Set the minimum and maximum number of length a input String parameter
 * may have.
 */

@Target(PARAMETER)
@Retention(RUNTIME)
public @interface Length {

    /**
     * @return The minimum number of characters the {@link String} may have.
     */

    long min() default 0;

    /**
     * @return The maximum number of characters the {@link String} may have.
     */

    long max() default Long.MAX_VALUE;
}
