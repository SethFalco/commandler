package com.elypia.commandler.exceptions;

public class ProviderRequiredException extends RuntimeException {

    public ProviderRequiredException() {
        super();
    }

    public ProviderRequiredException(String message, Object... args) {
        super(String.format(message, args));
    }

    public ProviderRequiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
