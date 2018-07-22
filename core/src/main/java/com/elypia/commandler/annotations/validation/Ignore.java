package com.elypia.commandler.annotations.validation;

import java.lang.annotation.*;

/**
 * This can be applied on a command to dictate it should ignore
 * any global validation applied by the module.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Ignore {

}
