package com.elypia.commandler.exceptions;

public class ProviderRequiredException extends RuntimeException {

    public ProviderRequiredException() {
        super();
    }

    public ProviderRequiredException(String message) {
        super(message);
    }

    public ProviderRequiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
