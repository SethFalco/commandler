package com.elypia.commandler.annotations.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Allows the {@link Param} annotiation to be repeatable
 * so we can specify multiple parameters per command.
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Params {
    Param[] value();
}
