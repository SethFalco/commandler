package com.elypia.commandler.parsing;

import com.elypia.commandler.data.SearchScope;
import com.elypia.commandler.events.MessageEvent;

public interface ParamParser<T> {
    T parse(MessageEvent event, SearchScope scope, String input) throws IllegalArgumentException;
}
