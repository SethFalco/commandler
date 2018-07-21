package com.elypia.commandler.annotations.validation.param;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.annotations.validation.Validation;

import java.lang.annotation.*;

/**
 * The {@link Partial} annotation is for when using any type of
 * array. When processing an array any null results are not-returned.
 * By default {@link Commandler} throw an error if even a single
 * item fails to parse, the Partial annotation can be used to simply
 * omit null results from parsing instead.
 * Even if partial is enabled, an error will still be thrown if
 * there are zero succesfully parsed results.
 */

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Validation
public @interface Partial {

}
