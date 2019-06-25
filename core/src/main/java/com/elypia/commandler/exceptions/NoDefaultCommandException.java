package com.elypia.commandler.exceptions;

import com.elypia.commandler.metadata.data.MetaModule;

import java.util.Objects;

public class NoDefaultCommandException extends Exception {

    private MetaModule module;

    public NoDefaultCommandException(MetaModule module) {
        this(module, null);
    }

    public NoDefaultCommandException(MetaModule module, String message, Object... args) {
        this(module, String.format(message, args), (Throwable)null);
    }

    public NoDefaultCommandException(MetaModule module, String message, Throwable cause) {
        super(message, cause);
        this.module = Objects.requireNonNull(module);
    }

    public MetaModule getModule() {
        return module;
    }
}
