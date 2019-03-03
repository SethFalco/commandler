package com.elypia.commandler.exceptions;

public class NoParserException extends CommandlerException {

    public NoParserException(Throwable cause, String message, Object... args) {
        super(cause, message, args);
    }

    public NoParserException(String message, Object... args) {
        super(message, args);
    }
}
