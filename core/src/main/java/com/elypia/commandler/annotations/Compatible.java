package com.elypia.commandler.annotations;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Compatible {

    Class<?>[] value();
}
