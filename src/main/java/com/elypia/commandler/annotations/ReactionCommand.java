package com.elypia.commandler.annotations;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ReactionCommand {

    /**
     * @return The reaction alias that triggers this command.
     */

    String reaction();

    /**
     * @return The name of the command this reaction should occur on.
     */

    String command();
}
