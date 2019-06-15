package com.elypia.commandler.annotations;

import com.elypia.commandler.metadata.data.MetaModule;

import java.lang.annotation.*;

/**
 * The Command annotation is used to supply metadata
 * to commands. This can be aliases, or the help to
 * let people know how to use this commands.
 *
 * All static data will be stored in an annotation, reserving the
 * method body for what it's meant for, logic.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

	/**
	 * @return The name of the commands as it appears in help / documentation.
	 * This also acts as a reference for other commands to refer to it.
	 */
	String name() default "";

	/**
	 * @return A list of all the alises that allow users
	 * to perform this commands, this must be not be registered in the
	 * {@link MetaModule} already.
	 */
	String[] aliases() default {};

	/**
	 * @return The help text to advise the user of what
	 * this commands does.
	 */
	String help() default "";

	/**
	 * @return If this module should be hidden from documentation.
	 */
	boolean hidden() default false;
}
