package org.elypia.commandler.exceptions;

public class ModuleNotFoundException extends RuntimeException {

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
