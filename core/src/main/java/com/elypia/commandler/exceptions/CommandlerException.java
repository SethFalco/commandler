package com.elypia.commandler.exceptions;

/**
 * An abstract class for building Commandler exceptions.
 * Provides some conviniences to make the code a bit cleaner.
 */
public abstract class CommandlerException extends Exception {

    public CommandlerException() {
        super();
    }

    public CommandlerException(String message, Object... args) {
        super(String.format(message, args));
    }

    public CommandlerException(String message, Throwable cause) {
        super(message, cause);
    }
}
