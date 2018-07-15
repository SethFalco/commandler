package com.elypia.commandler.string.building.builders;

import com.elypia.commandler.events.CommandEvent;
import com.elypia.commandler.string.building.IStringBuilder;

// ? Just named it DefaultBuilder to avoid clashing with Java's StringBuilder
public class DefaultBuilder implements IStringBuilder<String> {

    @Override
    public String build(CommandEvent event, String input) {
        return input;
    }
}
