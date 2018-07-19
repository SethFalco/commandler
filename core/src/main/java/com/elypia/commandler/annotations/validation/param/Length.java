package com.elypia.commandler.annotations.validation.param;

import com.elypia.commandler.annotations.validation.Validation;

import java.lang.annotation.*;

/**
 * Set the minimum and maximum number of characters
 * an input {@link String} parameter may have.
 */

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Validation
public @interface Length {

    /**
     * The default minimum number of characters allowed. <br>
     * (Can't physically have less than this anyways.)
     */

    int DEFAULT_MIN = 0;

    /**
     * The default maximium number of characters allowed.
     * Normally this is the only value that needs changing.
     */

    int DEFAULT_MAX = Short.MAX_VALUE;

    /**
     * @return The minimum number of characters the {@link String} may have.
     */

    int min() default DEFAULT_MIN;

    /**
     * @return The maximum number of characters the {@link String} may have.
     */

    int max() default DEFAULT_MAX;
}
