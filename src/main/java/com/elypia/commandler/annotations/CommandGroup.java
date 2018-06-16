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
public @interface CommandGroup {

    /**
     * During a default commands, it will default to the primary
     * commands of the CommandGroup. (The one that specifies the @Command
     * metadata in the first place.)
     *
     * @return The identifier for the CommandGroup.
     */

    String value();
}
