package com.elypia.commandler.test.impl.builders;

import com.elypia.commandler.annotations.Compatible;
import com.elypia.commandler.impl.CommandEvent;
import com.elypia.commandler.test.impl.ITestBuilder;

@Compatible({Number.class, double.class, float.class, long.class, int.class, short.class, byte.class})
public class NumberBuilderI implements ITestBuilder<Number> {

    @Override
    public String build(CommandEvent<String, String> event, Number output) {
        return String.valueOf(output);
    }
}
