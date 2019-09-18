package org.elypia.commandler.exceptions;

import org.elypia.commandler.event.ActionEvent;

import java.util.Objects;

public abstract class ActionException extends RuntimeException {

    private ActionEvent<?, ?> event;

    public ActionException(ActionEvent<?, ?> event) {
        super();
        this.event = Objects.requireNonNull(event);
    }

    public ActionException(ActionEvent<?, ?> event, String message) {
        super(message);
        this.event = Objects.requireNonNull(event);
    }

    public ActionException(ActionEvent<?, ?> event, String message, Throwable cause) {
        super(message, cause);
        this.event = Objects.requireNonNull(event);
    }

    public ActionException(ActionEvent<?, ?> event, Throwable cause) {
        super(cause);
        this.event = Objects.requireNonNull(event);
    }

    public ActionEvent<?, ?> getActionEvent() {
        return event;
    }
}
