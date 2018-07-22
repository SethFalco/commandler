package com.elypia.commandler.string.builders;

import com.elypia.commandler.impl.ICommandEvent;
import com.elypia.commandler.string.*;

public class NumberBuilder implements IStringBuilder<Number> {

    @Override
    public String build(ICommandEvent<?, ?, String> event, Number output) {
        return String.valueOf(output);
    }
}
