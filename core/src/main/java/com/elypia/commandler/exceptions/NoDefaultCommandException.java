package com.elypia.commandler.exceptions;

import com.elypia.commandler.metadata.MetaModule;

import java.util.Objects;

public class NoDefaultCommandException extends RuntimeException {

    private MetaModule module;

    public NoDefaultCommandException(MetaModule module) {
        this(module, null);
    }

    public NoDefaultCommandException(MetaModule module, String message) {
        this(module, message, null);
    }

    public NoDefaultCommandException(MetaModule module, String message, Throwable cause) {
        super(message, cause);
        this.module = Objects.requireNonNull(module);
    }

    public MetaModule getModule() {
        return module;
    }
}
