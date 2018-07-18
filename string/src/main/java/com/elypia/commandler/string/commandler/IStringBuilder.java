package com.elypia.commandler.string.commandler;

import com.elypia.commandler.CommandEvent;
import com.elypia.commandler.impl.IBuilder;

public interface IStringBuilder<I> extends IBuilder<I, String> {

    @Override
    String build(CommandEvent event, I input);
}
