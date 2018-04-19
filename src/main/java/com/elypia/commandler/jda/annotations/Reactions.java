package com.elypia.commandler.jda.annotations;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Reactions {
    Reaction[] value();
}
