package com.elypia.commandler.exceptions.misuse;

import com.elypia.commandler.Input;
import com.elypia.commandler.exceptions.ParamException;
import com.elypia.commandler.meta.data.MetaParam;

import java.util.*;

public class ListUnsupportedException extends ParamException {

    /** The particular <strong>item</strong> that failed to adapt.*/
    private List<String> items;

    public ListUnsupportedException(Input input, MetaParam param, List<String> items) {
        super(input, param);
        this.items = Objects.requireNonNull(items);
    }

    public List<String> getItems() {
        return items;
    }
}
