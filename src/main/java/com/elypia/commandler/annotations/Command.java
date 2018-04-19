package com.elypia.commandler.annotations;

import java.lang.annotation.*;

/**
 * The Command annotiation is used to supply metadata
 * to a command. This can be aliases, or the help string to
 * let people know how to use this command.
 *
 * All static data will be stored in an annotation, reserving the
 * method body for what it's meant for, functionality and dynamic data.
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

	/**
	 * @return A list of all the alises that allow users
	 * to perform this command.
	 */

	String[] aliases();

	/**
	 * @return A help string to advise the user of what
	 * this command does.
	 *
	 * If the help String is {@link String#isEmpty() empty} then
	 * it will be hidden from the help docs.
	 */

	String help() default "";
}
