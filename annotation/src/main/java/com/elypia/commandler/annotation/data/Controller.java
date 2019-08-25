package com.elypia.commandler.annotation.data;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.annotation.AnnotationUtils;

import java.lang.annotation.*;

/**
 * A {@link Controller} in {@link Commandler} is a subset of {@link Command}s
 * and how all {@link Command}s should be registered. <br>
 * See {@link Static} and {@link Default} for making simple commands
 * in {@link com.elypia.commandler.api.Controller}s.
 *
 * A {@link Controller} can be thought of as a module of commands.
 * A {@link Command} can be thought of as a single command.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Controller {

	/**
	 * @return The group this module belongs too, this is used for
	 * navigating the help menu.
	 */
	String group() default AnnotationUtils.EFFECTIVELY_NULL;

	/**
	 * @return If true the module will be hidden from help commands
	 * and documentation.
	 */
	boolean hidden() default false;
}
