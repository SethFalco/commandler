package com.elypia.commandler.console.builders;

import com.elypia.commandler.console.*;

// ? Just named it DefaultBuilder to avoid clashing with Java's StringBuilder
public class DefaultBuilder implements IStringBuilder<String> {

    @Override
    public String build(StringCommand event, String output) {
        return output;
    }
}
