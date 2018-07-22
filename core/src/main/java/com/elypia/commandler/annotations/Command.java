package com.elypia.commandler.annotations;

import com.elypia.commandler.Commandler;

import java.lang.annotation.*;

/**
 * The Command annotiation is used to supply metadata
 * to a commands. This can be aliases, or the help string to
 * let people know how to use this commands.
 *
 * All static data will be stored in an annotation, reserving the
 * method body for what it's meant for, logic.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

	/**
	 * The default value for {@link #id()}.
	 */
	int DEFAULT_ID = -1;

	/**
	 * The default value for {@link #help()}. By
	 * assigning this to {@link String#isEmpty() an empty
	 * String}, {@link Commandler} will omit this {@link Command}
	 * in the help and documentation. Assign it a value for it
	 * to be displayed.
	 */
	String DEFAULT_HELP = "";

	/**
	 * @return The unique ID of the command, this must be used to specify
	 * relational methods with this command, for example {@link Overload}.
	 * <strong>Note:</strong> If the ID is {@link #DEFAULT_ID} Commandler assumes there are
	 * no associations.
	 */
	int id() default DEFAULT_ID;

	/**
	 * @return The name of the commands as it appears in help / documentation.
	 */
	String name();

	/**
	 * @return A list of all the alises that allow users
	 * to perform this commands, this must be not be registered in the
	 * {@link Module} already.
	 */
	String[] aliases();

	/**
	 * @return A help string to advise the user of what
	 * this commands does.
	 *
	 * If the help String is {@link #DEFAULT_HELP} then
	 * it will be hidden from the help docs.
	 */
	String help() default DEFAULT_HELP;
}
