package com.elypia.commandler.test.integration.impl.builders;

import com.elypia.commandler.annotations.Compatible;
import com.elypia.commandler.test.integration.impl.TestProvider;

@Compatible({Number.class, double.class, float.class, long.class, int.class, short.class, byte.class})
public class NumberProvider implements TestProvider<Number> {

    @Override
    public String build(AbstractCommandlerEvent<String, String> event, Number output) {
        return String.valueOf(output);
    }
}
