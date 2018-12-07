package com.elypia.commandler.impl.builders;

import com.elypia.commandler.impl.*;

public class DefaultBuilder implements TestBuilder<String> {

    @Override
    public String build(TestEvent event, String output) {
        return output;
    }
}
