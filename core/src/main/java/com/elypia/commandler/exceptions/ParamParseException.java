package com.elypia.commandler.exceptions;

import com.elypia.commandler.event.ActionEvent;
import com.elypia.commandler.metadata.MetaParam;

import java.util.Objects;

public class ParamParseException extends ParamException {

    /** The particular <strong>item</strong> that failed to adapt.*/
    private String item;

    public ParamParseException(ActionEvent<?, ?> action, MetaParam metaParam, String item) {
        super(action, metaParam);
        this.item = Objects.requireNonNull(item);
    }

    public String getItem() {
        return item;
    }
}
