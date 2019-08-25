package com.elypia.commandler.exceptions;

import com.elypia.commandler.event.ActionEvent;

public class ParamCountMismatchException extends ActionException {

    public ParamCountMismatchException(ActionEvent<?, ?> event) {
        super(event);
    }
}
