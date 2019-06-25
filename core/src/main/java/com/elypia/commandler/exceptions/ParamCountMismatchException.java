package com.elypia.commandler.exceptions;

import com.elypia.commandler.Input;

public class ParamCountMismatchException extends InputException {

    public ParamCountMismatchException(Input input) {
        super(input);
    }
}
