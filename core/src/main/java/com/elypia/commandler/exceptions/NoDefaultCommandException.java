package com.elypia.commandler.exceptions;

import com.elypia.commandler.metadata.MetaController;

import java.util.Objects;

public class NoDefaultCommandException extends RuntimeException {

    private MetaController module;

    public NoDefaultCommandException(MetaController module) {
        this(module, null);
    }

    public NoDefaultCommandException(MetaController module, String message) {
        this(module, message, null);
    }

    public NoDefaultCommandException(MetaController module, String message, Throwable cause) {
        super(message, cause);
        this.module = Objects.requireNonNull(module);
    }

    public MetaController getModule() {
        return module;
    }
}
