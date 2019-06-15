package com.elypia.commandler.exceptions.misuse;

import com.elypia.commandler.Input;
import com.elypia.commandler.exceptions.InputException;

public class ParamCountMismatchException extends InputException {

    public ParamCountMismatchException(Input input) {
        super(input);
    }

    public ParamCountMismatchException(Input input, String message, Object... args) {
        super(input, message, args);
    }

    public ParamCountMismatchException(Input input, String message, Throwable cause) {
        super(input, message, cause);
    }
}
