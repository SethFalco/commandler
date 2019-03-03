package com.elypia.commandler.test.impl.builders;

import com.elypia.commandler.AbstractCommandlerEvent;
import com.elypia.commandler.annotations.Compatible;
import com.elypia.commandler.test.impl.TestBuilder;

@Compatible(String.class)
public class DefaultBuilder implements TestBuilder<String> {

    @Override
    public String build(AbstractCommandlerEvent<String, String> event, String output) {
        return output;
    }
}
