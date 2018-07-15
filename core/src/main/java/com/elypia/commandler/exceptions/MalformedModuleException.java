package com.elypia.commandler.exceptions;

/**
 * This will occur when Commandler finds a problem with how one of the
 * Modules (Command Handlers) were formed.
 */

public class MalformedModuleException extends RuntimeException {

    public MalformedModuleException() {
        super();
    }

    public MalformedModuleException(String message) {
        super(message);
    }

    public MalformedModuleException(String message, Throwable cause) {
        super(message, cause);
    }

    public MalformedModuleException(Throwable cause) {
        super(cause);
    }
}
