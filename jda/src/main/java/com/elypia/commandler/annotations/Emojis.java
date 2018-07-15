package com.elypia.commandler.annotations;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Emojis {
    Emoji[] value();
}
