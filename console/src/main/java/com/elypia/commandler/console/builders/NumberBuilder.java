package com.elypia.commandler.console.builders;

import com.elypia.commandler.console.*;

public class NumberBuilder implements IStringBuilder<Number> {

    @Override
    public String build(StringCommand event, Number output) {
        return String.valueOf(output);
    }
}
