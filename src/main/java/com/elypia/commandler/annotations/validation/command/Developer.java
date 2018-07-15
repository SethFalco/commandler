package com.elypia.commandler.annotations.validation.command;

import com.elypia.commandler.annotations.validation.Validation;

import java.lang.annotation.*;

/**
 * In the configuration developers can be specified.
 * This could be via a properties file or during runtime.
 * This allows the user to configure developer specific
 * modules or commands.
 */

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Validation("./resources/commands/developer.svg")
public @interface Developer {

}
