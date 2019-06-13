package com.elypia.commandler.exceptions.misuse;

import com.elypia.commandler.Input;
import com.elypia.commandler.metadata.data.ParamData;

import java.util.Objects;

public class ParamParseException extends ParamException {

    /** The particular <strong>item</strong> that failed to adapt.*/
    private String item;

    public ParamParseException(Input input, ParamData param, String item) {
        super(input, param);
        this.item = Objects.requireNonNull(item);
    }

    public String getItem() {
        return item;
    }
}
