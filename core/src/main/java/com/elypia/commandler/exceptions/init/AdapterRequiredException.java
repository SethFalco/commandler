package com.elypia.commandler.exceptions.init;

import com.elypia.commandler.exceptions.CommandlerRuntimeException;

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
