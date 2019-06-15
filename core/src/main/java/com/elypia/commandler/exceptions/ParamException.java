package com.elypia.commandler.exceptions;

import com.elypia.commandler.Input;
import com.elypia.commandler.metadata.data.MetaParam;

import java.util.Objects;

public abstract class ParamException extends InputException {

    /** The parameter metadata. */
    private MetaParam param;

    public ParamException(Input input, MetaParam param) {
        this(input, param, null);
    }

    public ParamException(Input input, MetaParam param, String message, Object... args) {
        this(input, param, String.format(message, args), (Throwable)null);
    }

    public ParamException(Input input, MetaParam param, String message, Throwable cause) {
        super(input, message, cause);
        this.param = Objects.requireNonNull(param);
    }

    public MetaParam getParam() {
        return param;
    }
}
