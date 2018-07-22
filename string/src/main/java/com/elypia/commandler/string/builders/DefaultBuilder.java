package com.elypia.commandler.string.builders;

import com.elypia.commandler.impl.ICommandEvent;
import com.elypia.commandler.string.IStringBuilder;

// ? Just named it DefaultBuilder to avoid clashing with Java's StringBuilder
public class DefaultBuilder implements IStringBuilder<String> {

    @Override
    public String build(ICommandEvent<?, ?, String> event, String output) {
        return output;
    }
}
