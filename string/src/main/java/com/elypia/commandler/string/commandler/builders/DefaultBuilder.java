package com.elypia.commandler.string.commandler.builders;

import com.elypia.commandler.CommandEvent;
import com.elypia.commandler.string.commandler.IStringBuilder;

// ? Just named it DefaultBuilder to avoid clashing with Java's StringBuilder
public class DefaultBuilder implements IStringBuilder<String> {

    @Override
    public String build(CommandEvent event, String input) {
        return input;
    }
}
