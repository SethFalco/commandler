package com.elypia.commandler.annotations;

import com.elypia.commandler.interfaces.IBuilder;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Builders {

    Class<? extends IBuilder>[] value();
}
