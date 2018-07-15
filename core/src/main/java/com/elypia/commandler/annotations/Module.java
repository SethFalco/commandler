package com.elypia.commandler.annotations;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Module {

	/**
	 * @return Name of the module as it should appear at the top
	 * of the help commands. This is not the alias of the module.
	 */

	String name();

	/**
	 * @return A list of all the alises that grant the
	 * user access to the module.
	 */

	String[] aliases();

	/**
	 * @return A help String to advise users what
	 * the module is for.
	 */

	String description();

	boolean hidden() default false;
}
