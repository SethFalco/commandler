package com.elypia.commandler.exceptions;

public class ModuleNotFoundException extends Exception {

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
