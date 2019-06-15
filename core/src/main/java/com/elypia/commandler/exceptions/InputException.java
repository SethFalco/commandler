package com.elypia.commandler.exceptions;

import com.elypia.commandler.Input;

import java.util.Objects;

public abstract class InputException extends CommandlerException {

    private Input input;

    public InputException(Input input) {
        this(input, null);
    }

    public InputException(Input input, String message, Object... args) {
        this(input, String.format(message, args), (Throwable)null);
    }

    public InputException(Input input, String message, Throwable cause) {
        super(message, cause);
        this.input = Objects.requireNonNull(input);
    }

    public Input getInput() {
        return input;
    }
}
