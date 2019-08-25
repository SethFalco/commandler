package com.elypia.commandler.annotation.data;

import com.elypia.commandler.annotation.AnnotationUtils;

import java.lang.annotation.*;

/**
 * The parameter annotation allows us to give parameters for commands
 * a name and short description for what the parameter is or
 * what you need.
 */
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Param {

	/** Set the default key to a literal string.*/
	String defaultValue() default AnnotationUtils.EFFECTIVELY_NULL;
}

