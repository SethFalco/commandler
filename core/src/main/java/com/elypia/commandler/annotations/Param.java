package com.elypia.commandler.annotations;

import java.lang.annotation.*;

/**
 * The parameter annotation allows us to give parameters for commands
 * a name and short description for what the parameter is or
 * what you need.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Param.List.class)
public @interface Param {

	String DEFAULT_HELP = "";

	/**
	 * The name to display this parameter as.
	 */
	String name();

	/**
	 * A small description of what the parameter is.
	 */
	String help() default DEFAULT_HELP;

	/**
	 * Allows the {@link Param} annotiation to be repeatable
	 * so we can specify multiple parameters per commands.
	 *
	 */
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	@interface List {
		Param[] value();
	}
}

