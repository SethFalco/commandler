package com.elypia.commandler.annotations;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Provider {

    /**
     * @return What this provider produces.
     */
    Class<?> provides();

    /**
     * @return The types this is compatible with working with.
     */
    Class<?>[] value();
}
