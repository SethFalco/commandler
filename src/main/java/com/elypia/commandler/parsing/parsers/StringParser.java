package com.elypia.commandler.parsing.parsers;

import com.elypia.commandler.data.SearchScope;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.parsing.IParamParser;

public class StringParser implements IParamParser<String> {

    @Override
    public String parse(MessageEvent event, SearchScope scope, String input) throws IllegalArgumentException {
        return input;
    }
}
