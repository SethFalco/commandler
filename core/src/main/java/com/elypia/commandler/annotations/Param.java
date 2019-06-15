package com.elypia.commandler.annotations;

import com.elypia.commandler.interfaces.DynDefaultValue;
import com.elypia.commandler.utils.Utils;

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
	String name() default "";

	/** A small description of what the parameter is. */
	String help() default "";

	/** Set the default value to a literal string.*/
	String defaultValue() default Utils.EFFECTIVELY_NULL;

	/** Set a dynamic default value by implementing and pointing to a {@link DynDefaultValue} implementation. */
	Class<? extends DynDefaultValue> dynDefaultValue() default DynDefaultValue.class;
}

