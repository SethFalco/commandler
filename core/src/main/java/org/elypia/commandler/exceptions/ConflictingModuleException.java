package org.elypia.commandler.exceptions;

public class ConflictingModuleException extends RuntimeException {

    public ConflictingModuleException() {
        super();
    }

    public ConflictingModuleException(String message) {
        super(message);
    }

    public ConflictingModuleException(String message, Throwable cause) {
        super(message, cause);
    }
}
