package org.elypia.commandler.exceptions;

import org.elypia.commandler.event.ActionEvent;
import org.elypia.commandler.metadata.MetaParam;

import java.util.*;

public class ListUnsupportedException extends ParamException {

    /** The particular <strong>item</strong> that failed to adapt.*/
    private List<String> items;

    public ListUnsupportedException(ActionEvent<?, ?> action, MetaParam metaParam, List<String> items) {
        super(action, metaParam);
        this.items = Objects.requireNonNull(items);
    }

    public List<String> getItems() {
        return items;
    }
}
