package com.elypia.commandler.annotations;

import com.elypia.commandler.modules.CommandHandler;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Module {

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

	/**
	 * Submodules are what come after module in the command. <br>
	 * The format will be similar to the following: <br>
	 * <code>&gt;twitch.notify add Rheannon96</code> <br>
	 * In this case <code>notify</code> is the submodule.
	 *
	 * @return A list of modules that are directly associated
	 * with this module to be put together in help.
	 */

	Class<? extends CommandHandler>[] submodules() default {

	};
}
