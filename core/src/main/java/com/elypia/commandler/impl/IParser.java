package com.elypia.commandler.impl;

import com.elypia.commandler.events.CommandEvent;

public interface IParser<O> {
    O parse(CommandEvent event, String input);
}
