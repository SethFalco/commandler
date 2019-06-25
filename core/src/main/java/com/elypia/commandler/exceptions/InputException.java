package com.elypia.commandler.exceptions;

import com.elypia.commandler.Input;

import java.util.Objects;

public abstract class InputException extends Exception {

    private Input input;

    public InputException(Input input) {
        super();
        this.input = Objects.requireNonNull(input);
    }

    public InputException(Input input, String message) {
        super(message);
        this.input = Objects.requireNonNull(input);
    }

    public InputException(Input input, String message, Throwable cause) {
        super(message, cause);
        this.input = Objects.requireNonNull(input);
    }

    public InputException(Input input, Throwable cause) {
        super(cause);
        this.input = Objects.requireNonNull(input);
    }

    public Input getInput() {
        return input;
    }
}
