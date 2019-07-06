package com.elypia.commandler.exceptions;

public class OnlyPrefixException extends Exception {

    public OnlyPrefixException() {
        super();
    }

    public OnlyPrefixException(String message) {
        super(message);
    }

    public OnlyPrefixException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
