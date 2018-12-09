package com.elypia.commandler.annotations;

import com.elypia.commandler.interfaces.IParser;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Parsers {

    Class<? extends IParser>[] value();
}
