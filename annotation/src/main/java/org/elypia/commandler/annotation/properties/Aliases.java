package org.elypia.commandler.annotation.properties;

import org.elypia.commandler.annotation.data.Property;
import org.elypia.commandler.dispatchers.StandardDispatcher;

import java.lang.annotation.*;

/**
 * Denotes that this is a module or command that can be accessed
 * under aliases.
 */
@PropertyWrapper(type = StandardDispatcher.class)
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Aliases {

    /**
     * @return The aliases for a command to execute this command.
     */
    @Property(key = "aliases")
    String[] value();
}
