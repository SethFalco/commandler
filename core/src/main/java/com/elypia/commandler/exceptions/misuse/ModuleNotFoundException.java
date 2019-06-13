package com.elypia.commandler.exceptions.misuse;

import com.elypia.commandler.exceptions.CommandlerException;

public class ModuleNotFoundException extends CommandlerException {

    public ModuleNotFoundException() {
        super();
    }

    public ModuleNotFoundException(String message) {
        super(message);
    }

    public ModuleNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
