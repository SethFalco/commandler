package com.elypia.commandler.impl;

import com.elypia.commandler.data.SearchScope;
import com.elypia.commandler.events.CommandEvent;

public interface IParser<T> {
    T parse(CommandEvent event, SearchScope scope, String input);
}
