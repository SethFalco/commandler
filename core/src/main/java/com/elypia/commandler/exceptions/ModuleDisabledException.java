package com.elypia.commandler.exceptions;

import com.elypia.commandler.Input;

public class ModuleDisabledException extends InputException {

    public ModuleDisabledException(Input input) {
        super(input);
    }

    public ModuleDisabledException(Input input, String message) {
        super(input, message);
    }

    public ModuleDisabledException(Input input, String message, Throwable cause) {
        super(input, message, cause);
    }
}
