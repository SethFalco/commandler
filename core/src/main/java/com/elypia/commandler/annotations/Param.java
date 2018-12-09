package com.elypia.commandler.annotations;

import com.elypia.commandler.Commandler;

import java.lang.annotation.*;

/**
 * The parameter annotation allows us to give parameters for commands
 * a id and short description for what the parameter is or
 * what you need.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Param.List.class)
public @interface Param {

	/**
	 * If the {@link #help()} is set to the default
	 * then {@link Commandler} will check a global list
	 * if a default there is a default help message for the
	 * data type of the parameter.
	 */
	String DEFAULT = "";

	/**
	 * The id to display this parameter as.
	 */
	String id();

	/**
	 * A small description of what the parameter is.
	 */
	String help() default DEFAULT;

	/**
	 * Allows the {@link Param} annotiation to be repeatable
	 * so we can specify multiple parameters per commands.
	 */
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	@interface List {

		/**
		 * @return Allows us to have multiple {@link Param}s
		 * per method.
		 */
		Param[] value();
	}
}

