package org.elypia.commandler.annotation.data;

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
	 * @return If this module should be hidden from documentation.
	 */
	boolean hidden() default false;
}
