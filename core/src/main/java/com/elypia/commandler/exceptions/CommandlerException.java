package com.elypia.commandler.exceptions;

/**
 * An abstract class for building Commandler exceptions.
 * Provides some conviniences to make the code a bit cleaner.
 */
public abstract class CommandlerException extends RuntimeException {

    public CommandlerException(Throwable cause, String message, Object... args) {
        super(String.format(message, args), cause);
    }

    public CommandlerException(String message, Object... args) {
        super(String.format(message, args));
    }
}
