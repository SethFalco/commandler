package com.elypia.commandler.annotations;

import com.elypia.commandler.*;

import java.lang.annotation.*;

/**
 * A {@link Module} in {@link Commandler} is a subset of {@link Command}s
 * and how most {@link Command}s should be registered. <br>
 * See {@link Static} and {@link Default} for making simple commands
 * though {@link Module}s.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Module {

	/**
	 * The default group which is assumed if the a group
	 * isn't otherwise specified.
	 */
	String DEFAULT_GROUP = "Miscellaneous";

	/**
	 * The default help console for modules. By leaving the
	 * {@link #help()} as an empty {@link String} it is omitted
	 * from the help commands and documentation. Assign this a value
	 * to be displayed.
	 */
	String DEFAULT_HELP = "";

	/**
	 * @return Name of the module as it should appear at the top
	 * of the help commands. This is not the alias of the module.
	 */
	String name();

	/**
	 * @return The group this module belongs too, this is used for
	 * navigating the help menu.
	 */
	String group() default DEFAULT_GROUP;

	/**
	 * @return A list of all the alises that grant the
	 * user access to the module, these must be unique, the same
	 * alias can not be registered to two modules.
	 */
	String[] aliases();

	/**
	 * @return A help String to advise users what
	 * the module is for, if this is {@link String#isEmpty()}
	 * then the module is hidden from any help commands or documentation.
	 */
	String help() default DEFAULT_HELP;

	/**
	 * All {@link Module}s can assign themself a parent module recursively.
	 * This is how submodules are created.
	 *
	 * @return The parent class this is a child of. Or {@link Handler} if
	 * this has no parent module.
	 */
	Class<? extends Handler> parent() default Handler.class;
}
