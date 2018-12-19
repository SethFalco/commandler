package com.elypia.commandler.annotations;

import java.lang.annotation.*;

/**
 * Select the icon that represents this module.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Icon {

    String value();

    String color() default "#FFFFFF";
}
