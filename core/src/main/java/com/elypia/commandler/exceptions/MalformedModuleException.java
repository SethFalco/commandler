package com.elypia.commandler.exceptions;

/**
 * Occurs when a module is not missing required metadata
 * after building.
 */
public class MalformedModuleException extends CommandlerRuntimeException {

    public MalformedModuleException() {
        super();
    }

    public MalformedModuleException(String message, Object... args) {
        super(String.format(message, args));
    }

    public MalformedModuleException(String message, Throwable cause) {
        super(message, cause);
    }
}
