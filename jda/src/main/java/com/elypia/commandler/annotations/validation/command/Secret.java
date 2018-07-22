package com.elypia.commandler.annotations.validation.command;

import com.elypia.commandler.annotations.validation.Validation;

import java.lang.annotation.*;

/**
 * This defines commands that may require secret information.
 * Secret information could be API credentials, or some kind of
 * password, pin, code, or token.
 * The default implementation of secrets is to delete the message
 * if performed in a chat that may include anyone but the bot, and the
 * user performing the command.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Validation
public @interface Secret {

}
