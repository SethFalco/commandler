package com.elypia.commandler.annotations;

import java.lang.annotation.*;

/**
 * Allows the {@link Param} annotiation to be repeatable
 * so we can specify multiple parameters per command.
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Params {
    Param[] value();
}
