package com.elypia.commandler.annotations;

import java.lang.annotation.*;
import java.lang.reflect.Parameter;

/**
 * The parameter annotation allows us to give parameters for commands
 * a name and short description for what the parameter is or
 * what you need.
 */
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Param {

	/**
	 * The name to display this parameter as, this can be specified
	 * otherwise the {@link Parameter#getName()} value is used. <br>
	 * <strong>This can be left blank if the compiler argument -parameters is used.</strong>
	 */
	String name() default "";

	/**
	 * A small description of what the parameter is.
	 */
	String value() default "";
}

