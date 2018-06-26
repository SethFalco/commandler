package com.elypia.commandler.annotations;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Reaction {

    int id();

    String[] emotes();
}
