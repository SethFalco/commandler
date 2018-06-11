package com.elypia.commandler.exceptions;

public class RecursiveAliasException extends RuntimeException {

    public RecursiveAliasException() {
        super();
    }

    public RecursiveAliasException(String message) {
        super(message);
    }

    public RecursiveAliasException(String message, Throwable cause) {
        super(message, cause);
    }

    public RecursiveAliasException(Throwable cause) {
        super(cause);
    }
}
