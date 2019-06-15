package com.elypia.commandler.exceptions.init;

import com.elypia.commandler.exceptions.CommandlerRuntimeException;

public class BuilderRequiredException extends CommandlerRuntimeException {

    public BuilderRequiredException() {
        super();
    }

    public BuilderRequiredException(String message, Object... args) {
        super(String.format(message, args));
    }

    public BuilderRequiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
