package com.elypia.commandler.annotations;

import java.lang.annotation.*;

/**
 * The parameter annotation allows us to give parameters for commands
 * a name and short description for what the parameter is or
 * what you need.
 */ // ? Named Param to not conflict with Java's Parameter.
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Params.class)
public @interface Param {

	/**
	 * The name to display this parameter as.
	 */
	String name();

	/**
	 * A small description of what the parameter is.
	 */
	String help();
}
