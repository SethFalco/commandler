package com.elypia.commandler.impl;

import com.elypia.commandler.data.SearchScope;
import com.elypia.commandler.events.CommandEvent;

public interface MisuseListener {

    <T> String parameterParseFailure(IParser<T> parser, Class<T> type, CommandEvent event, SearchScope scope, String input);

    String ;
}
