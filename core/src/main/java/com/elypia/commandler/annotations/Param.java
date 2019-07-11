package com.elypia.commandler.annotations;

import com.elypia.commandler.utils.AnnoUtils;

import java.lang.annotation.*;

/**
 * The parameter annotation allows us to give parameters for commands
 * a name and short description for what the parameter is or
 * what you need.
 */
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Param {

	/** The name to display this parameter as. */
	String name() default AnnoUtils.EFFECTIVELY_NULL;

	/** A small description of what the parameter is. */
	String help() default AnnoUtils.EFFECTIVELY_NULL;

	/** Set the default value to a literal string.*/
	String defaultValue() default AnnoUtils.EFFECTIVELY_NULL;
}

