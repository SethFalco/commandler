package org.elypia.commandler.annotation.data;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Messenger {

    /**
     * @return What this provider produces.
     */
    Class<?> provides();

    /**
     * @return The types this is compatible with working with.
     */
    Class<?>[] value();
}
