package com.elypia.commandler.jda.annotations;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Emojis.class)
public @interface Emoji {

	/**
	 * @return The reaction to post after the commands has executed as a String.
	 */
	String[] emotes();

	/**
	 * @return What will occur when the user performs this reaction on the message.
	 */
	String help();
}
