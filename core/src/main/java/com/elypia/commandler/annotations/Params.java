package com.elypia.commandler.annotations;

import java.lang.annotation.*;

/**
 * Used for mapping an param object rather than
 * having multiple params in a method signature.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Params {

}
