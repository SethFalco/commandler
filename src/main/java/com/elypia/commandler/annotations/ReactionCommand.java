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
     * @return The type of event this reaction command is applicable for.
     */

    String event();
}
