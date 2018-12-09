package com.elypia.commandler.annotations;

import com.elypia.commandler.Commandler;

import java.lang.annotation.*;

/**
 * The Command annotiation is used to supply metadata
 * to a commands. This can be aliases, or the help to
 * let people know how to use this commands.
 *
 * All static data will be stored in an annotation, reserving the
 * method body for what it's meant for, logic.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

	/**
	 * The default value for {@link #help()}. By
	 * assigning this to {@link String#isEmpty() an empty
	 * String}, {@link Commandler} will omit this {@link Command}
	 * in the help and documentation. Assign it a value for it
	 * to be displayed.
	 */
	String DEFAULT_HELP = "";

	/**
	 * @return The id of the commands as it appears in help / documentation.
	 * 		   This also acts as a reference for other commands to refer to it.
	 */
	String id();

	/**
	 * @return A list of all the alises that allow users
	 * to perform this commands, this must be not be registered in the
	 * {@link Module} already.
	 */
	String[] aliases();

	/**
	 * @return The help text to advise the user of what
	 * this commands does.
	 *
	 * If the help String is {@link #DEFAULT_HELP} then
	 * it will be hidden from the help docs.
	 */
	String help() default DEFAULT_HELP;
}
