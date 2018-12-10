package com.elypia.commandler.doc.annotations;

import java.lang.annotation.*;

/**
 * Show the usage of the command and example result.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Example {

    String command();

    String response();
}
