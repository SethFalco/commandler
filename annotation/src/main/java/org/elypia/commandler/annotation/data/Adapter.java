package org.elypia.commandler.annotation.data;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Adapter {

    /**
     * @return The types this is compatible with working with.
     */
    Class<?>[] value();
}
