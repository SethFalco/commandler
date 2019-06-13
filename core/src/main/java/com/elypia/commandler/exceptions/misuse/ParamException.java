package com.elypia.commandler.exceptions.misuse;

import com.elypia.commandler.Input;
import com.elypia.commandler.metadata.data.ParamData;

import java.util.Objects;

public abstract class ParamException extends InputException {

    /** The parameter metadata. */
    private ParamData param;

    public ParamException(Input input, ParamData param) {
        this(input, param, null);
    }

    public ParamException(Input input, ParamData param, String message, Object... args) {
        this(input, param, String.format(message, args), (Throwable)null);
    }

    public ParamException(Input input, ParamData param, String message, Throwable cause) {
        super(input, message, cause);
        this.param = Objects.requireNonNull(param);
    }

    public ParamData getParam() {
        return param;
    }
}
