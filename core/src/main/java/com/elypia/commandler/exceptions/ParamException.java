package com.elypia.commandler.exceptions;

import com.elypia.commandler.Input;
import com.elypia.commandler.metadata.data.MetaParam;

import java.util.Objects;

public abstract class ParamException extends InputException {

    /** The parameter metadata. */
    private MetaParam param;

    public ParamException(Input input, MetaParam param) {
        super(input);
        this.param = Objects.requireNonNull(param);
    }

    public ParamException(Input input, MetaParam param, String message) {
        super(input, message);
        this.param = Objects.requireNonNull(param);
    }

    public ParamException(Input input, MetaParam param, String message, Throwable cause) {
        super(input, message, cause);
        this.param = Objects.requireNonNull(param);
    }

    public ParamException(Input input, MetaParam param, Throwable cause) {
        super(input, cause);
        this.param = Objects.requireNonNull(param);
    }

    public MetaParam getParam() {
        return param;
    }
}
