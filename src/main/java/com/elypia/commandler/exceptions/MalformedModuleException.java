package com.elypia.commandler.exceptions;

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
