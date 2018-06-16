package com.elypia.commandler.exceptions;

public class MalformedCommandException extends RuntimeException {

    public MalformedCommandException() {
        super();
    }

    public MalformedCommandException(String message) {
        super(message);
    }

    public MalformedCommandException(String message, Throwable cause) {
        super(message, cause);
    }

    public MalformedCommandException(Throwable cause) {
        super(cause);
    }
}
