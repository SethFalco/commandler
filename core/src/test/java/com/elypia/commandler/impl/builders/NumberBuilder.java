package com.elypia.commandler.impl.builders;

import com.elypia.commandler.annotations.Compatible;
import com.elypia.commandler.impl.*;

@Compatible({Number.class, double.class, float.class, long.class, int.class, short.class, byte.class})
public class NumberBuilder implements TestBuilder<Number> {

    @Override
    public String build(CommandEvent<Void, String, String> event, Number output) {
        return String.valueOf(output);
    }
}
