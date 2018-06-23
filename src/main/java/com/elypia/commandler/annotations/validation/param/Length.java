package com.elypia.commandler.annotations.validation.param;

import com.elypia.commandler.annotations.Validation;

import java.lang.annotation.*;

/**
 * Set the minimum and maximum number of length a input String parameter
 * may have.
 */

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Validation("./resources/params/length.svg")
public @interface Length {

    /**
     * @return The minimum number of characters the {@link String} may have.
     */

    int min() default 0;

    /**
     * @return The maximum number of characters the {@link String} may have.
     */

    int max() default Integer.MAX_VALUE;
}
