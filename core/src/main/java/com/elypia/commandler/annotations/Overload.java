package com.elypia.commandler.annotations;

import java.lang.annotation.*;

/**
 * Command groups are used when overloading commands, ie there is
 * the same commands multiple times; just with different parameters.
 * Rather than create metadata for these commands multiple times, we
 * can associate it with an {@link Overload}, and it will copy some data
 * from the main {@link Command}.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Overload {

    /**
     * Before you can use this you must ensure the command you are overloading
     * specified the {@link Command#name()} value. That is the unique reference to the
     * command and how Commandler knows what command this is overloading.
     */
    String value();
}
