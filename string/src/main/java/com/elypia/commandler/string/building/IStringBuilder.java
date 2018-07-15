package com.elypia.commandler.string.building;

import com.elypia.commandler.events.CommandEvent;
import com.elypia.commandler.impl.IBuilder;

public interface IStringBuilder<I> extends IBuilder<I, String> {

    @Override
    String build(CommandEvent event, I input);
}
