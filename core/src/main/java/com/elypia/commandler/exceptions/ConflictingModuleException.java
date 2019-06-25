package com.elypia.commandler.exceptions;

public class ConflictingModuleException extends RuntimeException {

    public ConflictingModuleException() {
        super();
    }

    public ConflictingModuleException(String message, Object... args) {
        super(String.format(message, args));
    }

    public ConflictingModuleException(String message, Throwable cause) {
        super(message, cause);
    }
}
