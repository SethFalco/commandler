package com.elypia.commandler.annotations;

import java.lang.annotation.*;

/**
 * Command groups are used when overloading commands, ie there is
 * the same commands multiple times; just with different parameters.
 * Rather than create metadata for these commands multiple times, we
 * can associate it with a CommandGroup, and it will copy the data from
 * the main Command.
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Overload {

    String INHERIT = "";
    String[] INHERIT_NONE = {};

    /**
     * Before you can use this you must ensure the command you are overloading
     * specified the {@link Command#id()} value. That is the unique reference to the
     * command and how Commandler knows what command this is overloading.
     */

    int value();

    /**
     * This dictates which parameters are copied from the parent {@link Command}
     * and in what order. If {@link #INHERIT} (default) is specified, then
     * it copies all parent params in the same order, and appends any new ones
     * in the overload, otherwise you can specify a String[] specifying a list
     * of params in the order the overload required them.
     */

    String[] params() default INHERIT;

    String[] emotes() default INHERIT;

    /**
     * This dictates which validators are copied from the parent {@link Command}.
     * This copies each validator by class. <br>
     * If an empty array is passed, no validators are copied. <br>
     * If {@link Overload} is passed (default), all validators are copied
     * unless otherwise overridden by the overloading method.
     */

    Class<? extends Annotation>[] validation() default Overload.class;
}
