package com.elypia.commandler.exceptions;

/**
 * Occurs when a module is not missing required metadata
 * after building.
 */
public class MalformedModuleException extends CommandlerException {

    public MalformedModuleException(Throwable cause, String message, Object... args) {
        super(cause, message, args);
    }

    public MalformedModuleException(String message, Object... args) {
        super(message, args);
    }
}
