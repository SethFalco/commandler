package com.elypia.commandler.exceptions;

import com.elypia.commandler.annotations.Validation;

/**
 * This will occur if you try to register a command or parameter validator
 * using an annotation without the @Validator annotation.
 * The {@link Validation} annotation allows Commandler to recognise it as
 * as a validator and allows the icon associated with it to be configured.
 */

public class MalformedValidatorException extends RuntimeException {

    public MalformedValidatorException() {
        super();
    }

    public MalformedValidatorException(String message) {
        super(message);
    }

    public MalformedValidatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public MalformedValidatorException(Throwable cause) {
        super(cause);
    }
}
