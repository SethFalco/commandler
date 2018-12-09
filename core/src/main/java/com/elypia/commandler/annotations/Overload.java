package com.elypia.commandler.annotations;

import com.elypia.commandler.Commandler;

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
     * By setting a value to a single empty console,
     * {@link Commandler} will interpret this as inherit
     * all of the {@link Command}'s data.
     */
    String INHERIT = "";

    /**
     * Before you can use this you must ensure the command you are overloading
     * specified the {@link Command#id()} value. That is the unique reference to the
     * command and how Commandler knows what command this is overloading.
            */
    String value();

    /**
     * This dictates which parameters are copied from the parent {@link Command}
     * and in what order. If {@link #INHERIT} (default) is specified, then
     * it copies all parent params in the same order, and appends any new ones
     * in the overload, otherwise you can specify a String[] specifying a list
     * of params in the order the overload required them. If an empty array
     * is passed, no parameters is inherited.
     */
    String[] params() default INHERIT;
}
