package com.elypia.commandler.test.impl.builders;

import com.elypia.commandler.annotations.Compatible;
import com.elypia.commandler.impl.CommandEvent;
import com.elypia.commandler.test.impl.ITestBuilder;

@Compatible(String.class)
public class DefaultBuilderI implements ITestBuilder<String> {

    @Override
    public String build(CommandEvent<String, String> event, String output) {
        return output;
    }
}
