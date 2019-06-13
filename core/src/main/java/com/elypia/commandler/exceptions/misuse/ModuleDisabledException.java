package com.elypia.commandler.exceptions.misuse;

import com.elypia.commandler.Input;

public class ModuleDisabledException extends InputException {

    public ModuleDisabledException(Input input) {
        super(input);
    }

    public ModuleDisabledException(Input input, String message, Object... args) {
        super(input, message, args);
    }

    public ModuleDisabledException(Input input, String message, Throwable cause) {
        super(input, message, cause);
    }
}
