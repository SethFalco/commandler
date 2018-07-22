package com.elypia.commandler.string.builders;

import com.elypia.commandler.impl.ICommandEvent;
import com.elypia.commandler.string.IStringBuilder;

public class NumberBuilder implements IStringBuilder<Number> {

    @Override
    public String build(ICommandEvent<?, ?, String> event, Number input) {
        return String.valueOf(input);
    }
}
