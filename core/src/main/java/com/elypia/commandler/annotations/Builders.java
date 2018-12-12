package com.elypia.commandler.annotations;

import com.elypia.commandler.*;
import com.elypia.commandler.interfaces.IBuilder;

import java.lang.annotation.*;

/**
 * Apply this on {@link Handler} so {@link Commandler}
 * can determine what {@link IBuilder}s each {@link Module}
 * depends on.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Builders {

    /**
     * @return The {@link IBuilder}s this {@link Module}
     * depends on. These are used to parse return type
     * values or into viable messages to send.
     */
    Class<? extends IBuilder>[] value();
}
