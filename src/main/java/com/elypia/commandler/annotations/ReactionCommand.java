package com.elypia.commandler.annotations;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ReactionCommand {

    /**
     * @return The reaction alias that triggers this commands.
     */

    String reaction();

    /**
     * @return The name of the commands this reaction should occur on.
     */

    String command();
}
