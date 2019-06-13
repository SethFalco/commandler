package com.elypia.commandler.exceptions;

public class AdapterRequiredException extends CommandlerRuntimeException {

    public AdapterRequiredException() {
        super();
    }

    public AdapterRequiredException(String message, Object... args) {
        super(String.format(message, args));
    }

    public AdapterRequiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
