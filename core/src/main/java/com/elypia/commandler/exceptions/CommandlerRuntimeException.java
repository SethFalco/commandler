package com.elypia.commandler.exceptions;

/**
 * An abstract class for building Commandler exceptions.
 * Provides some conviniences to make the code a bit cleaner.
 */
public abstract class CommandlerRuntimeException extends RuntimeException {

    public CommandlerRuntimeException() {
        super();
    }

    public CommandlerRuntimeException(String message, Object... args) {
        super(String.format(message, args));
    }

    public CommandlerRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
