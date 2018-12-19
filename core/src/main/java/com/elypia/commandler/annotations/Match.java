package com.elypia.commandler.annotations;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Match {

    /**
     * @return The regular expression that will be used to
     * match portions of the event.
     */
    String value();
}
