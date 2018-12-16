package com.elypia.commandler.test.impl.builders;

import com.elypia.commandler.annotations.Compatible;
import com.elypia.commandler.impl.CommandEvent;
import com.elypia.commandler.test.impl.TestBuilder;

@Compatible({Number.class, double.class, float.class, long.class, int.class, short.class, byte.class})
public class NumberBuilder implements TestBuilder<Number> {

    @Override
    public String build(CommandEvent<String, String> event, Number output) {
        return String.valueOf(output);
    }
}
