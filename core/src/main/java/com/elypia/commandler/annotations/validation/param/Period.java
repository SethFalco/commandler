package com.elypia.commandler.annotations.validation.param;

import com.elypia.commandler.annotations.validation.Validation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * Limit the length of a timespan the user specified.
 */

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Validation
public @interface Period {

    /**
     * The default minimum amount of time the user can specify. <br>
     * (You shouldn't be able to go back in time so not below 0!)
     */

    long DEFAULT_MIN = 0;

    /**
     * The default maximum amount of time the user can specify.
     */

    long DEFAULT_MAX = Long.MAX_VALUE;

    /**
     * @return The minimum amount of time the user can specify.
     */

    long min() default DEFAULT_MIN;

    /**
     * @return The maxmimum amount of time the user can specify.
     */

    long max() default DEFAULT_MAX;

    /**
     * @return The unit of time used for this validation, this is
     *         {@link TimeUnit#SECONDS} by default.
     */

    TimeUnit unit() default TimeUnit.SECONDS;
}
