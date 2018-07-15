package com.elypia.commandler.exceptions;

/**
 * This will occur when Commandler finds a problem with how one of the
 * Commands were formed.
 */

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
