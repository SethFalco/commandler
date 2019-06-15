package com.elypia.commandler.exceptions.init;

import com.elypia.commandler.exceptions.CommandlerRuntimeException;

public class ConflictingModuleException extends CommandlerRuntimeException {

    public ConflictingModuleException() {
        super();
    }

    public ConflictingModuleException(String message, Object... args) {
        super(String.format(message, args));
    }

    public ConflictingModuleException(String message, Throwable cause) {
        super(message, cause);
    }
}
