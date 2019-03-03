package com.elypia.commandler.exceptions;

public class ConflictingModuleException extends CommandlerException {

    public ConflictingModuleException(Throwable cause, String message, Object... args) {
        super(cause, message, args);
    }

    public ConflictingModuleException(String message, Object... args) {
        super(message, args);
    }
}
