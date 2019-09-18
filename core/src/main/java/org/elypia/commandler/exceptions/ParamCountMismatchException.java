package org.elypia.commandler.exceptions;

import org.elypia.commandler.event.ActionEvent;

public class ParamCountMismatchException extends ActionException {

    public ParamCountMismatchException(ActionEvent<?, ?> event) {
        super(event);
    }
}
