package com.elypia.commandler.annotations.command;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Reactions.class)
public @interface Reaction {

	String alias();
	String help();
}
