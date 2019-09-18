package org.elypia.commandler.exceptions;

import org.elypia.commandler.event.ActionEvent;
import org.elypia.commandler.metadata.MetaParam;

import java.util.Objects;

public abstract class ParamException extends ActionException {

    /** The parameter metadata. */
    private MetaParam metaParam;

    public ParamException(ActionEvent<?, ?> action, MetaParam metaParam) {
        super(action);
        this.metaParam = Objects.requireNonNull(metaParam);
    }

    public ParamException(ActionEvent<?, ?> action, MetaParam metaParam, String message) {
        super(action, message);
        this.metaParam = Objects.requireNonNull(metaParam);
    }

    public ParamException(ActionEvent<?, ?> action, MetaParam metaParam, String message, Throwable cause) {
        super(action, message, cause);
        this.metaParam = Objects.requireNonNull(metaParam);
    }

    public ParamException(ActionEvent<?, ?> action, MetaParam metaParam, Throwable cause) {
        super(action, cause);
        this.metaParam = Objects.requireNonNull(metaParam);
    }

    public MetaParam getMetaParam() {
        return metaParam;
    }
}
