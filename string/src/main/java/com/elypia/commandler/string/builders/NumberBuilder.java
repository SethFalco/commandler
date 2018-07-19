package com.elypia.commandler.string.builders;

import com.elypia.commandler.CommandEvent;
import com.elypia.commandler.string.IStringBuilder;

public class NumberBuilder implements IStringBuilder<Number> {

    @Override
    public String build(CommandEvent event, Number input) {
        return String.valueOf(input);
    }
}
