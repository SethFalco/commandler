package com.elypia.commandler.exceptions;

public class AdapterRequiredException extends RuntimeException {

    public AdapterRequiredException() {
        super();
    }

    public AdapterRequiredException(String message) {
        super(message);
    }

    public AdapterRequiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
