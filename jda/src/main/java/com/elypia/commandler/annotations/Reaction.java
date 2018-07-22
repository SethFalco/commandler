package com.elypia.commandler.annotations;

import java.lang.annotation.*;

/**
 * The @Reaction annotation is used for reaction handling.
 * A command that has reaction handling must specify a non -1
 * ID. The reaction event can specify the id of the command it's for
 * and then the emotes are valid for this method.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Reaction {

    /**
     * The ID of the command this reaction event handles.

     * @see Command#id() The ID this corresponds with.
     * @return The command ID this reaction event handles reactions for.
     */
    int id();

    /**
     * The reactions that can trigger this reaction command.
     */
    String[] emotes();
}
