package com.elypia.commandler.annotations;

import com.elypia.commandler.Handler;
import com.elypia.commandler.interfaces.IParser;

import java.lang.annotation.*;

/**
 * Used on {@link Handler} to idenfity what {@link IParser}s
 * the {@link Module} depends on.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Parsers {

    /**
     * @return Each {@link IParser} the {@link Module}
     * depends on in order to work.
     */
    Class<? extends IParser>[] value();
}
