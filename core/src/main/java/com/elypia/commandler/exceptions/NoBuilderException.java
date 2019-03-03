package com.elypia.commandler.exceptions;

public class NoBuilderException extends CommandlerException {

    public NoBuilderException(Throwable cause, String message, Object... args) {
        super(cause, message, args);
    }

    public NoBuilderException(String message, Object... args) {
        super(message, args);
    }
}
