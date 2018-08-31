package com.elypia.commandler.jda.annotations.validation.command;

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
public @interface Secret {

}
