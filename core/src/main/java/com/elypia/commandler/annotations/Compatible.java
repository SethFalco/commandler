package com.elypia.commandler.annotations;

import com.elypia.commandler.interfaces.*;

import java.lang.annotation.*;
import java.time.Duration;

/**
 * Used with {@link Parser}s and {@link Builder}s in
 * order to bind data-types they are made to work with.
 * For example if a builder is able to convert your
 * {@link Duration} into a viable message object for your
 * respective platform, then you'd add
 * <code>Duration.class</code> to the {@link Compatible} annotation.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Compatible {

    /**
     * @return The types this is compatible with working with.
     */
    Class<?>[] value();
}
