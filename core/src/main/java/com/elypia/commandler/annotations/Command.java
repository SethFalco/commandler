package com.elypia.commandler.annotations;

import java.lang.annotation.*;

/**
 * The Command annotiation is used to supply metadata
 * to a commands. This can be aliases, or the help string to
 * let people know how to use this commands.
 *
 * All static data will be stored in an annotation, reserving the
 * method body for what it's meant for, functionality and dynamic data.
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

	/**
	 * @return the internal id used to manage this command, this must
	 * be used to specify associated methods with this command, for example
	 * {@link Overload}, if the ID is -1 (default) Commandler assumes there are
	 * no associations.
	 */

	int id() default -1;

	/**
	 * @return The name of the commands as it appears in help / documentation.
	 */

	String name();

	/**
	 * @return A list of all the alises that allow users
	 * to perform this commands.
	 */

	String[] aliases();

	/**
	 * @return A help string to advise the user of what
	 * this commands does.
	 *
	 * If the help String is {@link String#isEmpty() empty} then
	 * it will be hidden from the help docs.
	 */

	String help() default "";
}
