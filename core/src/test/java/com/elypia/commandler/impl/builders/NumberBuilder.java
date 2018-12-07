package com.elypia.commandler.impl.builders;

import com.elypia.commandler.impl.*;

public class NumberBuilder implements TestBuilder<Number> {

    @Override
    public String build(TestEvent event, Number output) {
        return String.valueOf(output);
    }
}
