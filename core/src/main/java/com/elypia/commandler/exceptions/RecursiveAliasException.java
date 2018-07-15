package com.elypia.commandler.exceptions;

/**
 * This will occur when Commandler finds you're trying to register
 * an alias which has already been registered by another module or static command. <br>
 * This is a problem as Commandler will not know which to execute should the user try to.
 */

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
