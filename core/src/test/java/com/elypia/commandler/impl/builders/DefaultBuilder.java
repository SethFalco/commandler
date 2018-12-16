package com.elypia.commandler.impl.builders;

import com.elypia.commandler.annotations.Compatible;
import com.elypia.commandler.impl.*;

@Compatible(String.class)
public class DefaultBuilder implements TestBuilder<String> {

    @Override
    public String build(CommandEvent<String, String> event, String output) {
        return output;
    }
}
