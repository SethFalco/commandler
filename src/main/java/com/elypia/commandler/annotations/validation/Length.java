package com.elypia.commandler.annotations.validation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(PARAMETER)
@Retention(RUNTIME)
public @interface Length {

    /**
     * @return The minimum value that the parameter can be, inclusive.
     */

    long min() default 0;

    /**
     * @return The maximum value that the parameter can be, inclusive.
     */

    long max() default Long.MAX_VALUE;
}
