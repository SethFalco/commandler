package com.elypia.commandler.annotation.properties;

import com.elypia.commandler.annotation.data.*;
import com.elypia.commandler.dispatchers.MatchDispatcher;

import java.lang.annotation.*;

@PropertyWrapper(type = MatchDispatcher.class)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Match {

    /**
     * @return The regular expression that will be used to
     * match portions of the event.
     */
    @Property(key = "pattern")
    String value();
}
