package com.elypia.commandler.annotations;

import com.elypia.commandler.*;

import java.lang.annotation.*;

/**
 * A {@link Module} in {@link Commandler} is a subset of {@link Command}s
 * and how most {@link Command}s should be registered. <br>
 * See {@link Static} and {@link Default} for making simple commands
 * though {@link Handler}s.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Module {

	/**
	 * @return Name of the module as it should appear at the top
	 * of the value commands. This is not the alias of the module.
	 */
	String name() default "";

	/**
	 * @return The group this module belongs too, this is used for
	 * navigating the help menu.
	 */
	String group() default "";

	/**
	 * @return A list of all the alises that grant the
	 * user access to the module, these must be unique, the same
	 * alias can not be registered in two modules.
	 */
	String[] aliases() default {};

	/**
	 * @return A value String to advise users what
	 * the module is for.
	 */
	String help() default "";

	/**
	 * @return If true the module will be hidden from help commands
	 * and documentation.
	 */
	boolean hidden() default false;
}
