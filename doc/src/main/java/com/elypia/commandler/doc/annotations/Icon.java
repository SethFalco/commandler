package com.elypia.commandler.doc.annotations;

import java.lang.annotation.*;

/**
 * Select the icon that represents this module.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Icon {

    String icon();

    String color() default "#FFFFFF";
}
