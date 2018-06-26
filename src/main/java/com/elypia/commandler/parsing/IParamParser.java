package com.elypia.commandler.parsing;

import com.elypia.commandler.data.SearchScope;
import com.elypia.commandler.events.CommandEvent;

public interface IParamParser<T> {
    T parse(CommandEvent event, SearchScope scope, String input);
}
