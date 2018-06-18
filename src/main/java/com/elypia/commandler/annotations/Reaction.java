package com.elypia.commandler.annotations;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Reactions.class)
public @interface Reaction {

	/**
	 * @return The reaction to post after the commands has executed as a String.
	 */

	String alias();

	/**
	 * @return What will occur when the user performs this reaction on the message.
	 */

	String help() default "";
}
