package com.elypia.commandler.parsing.parsers;

import com.elypia.commandler.data.SearchScope;
import com.elypia.commandler.events.CommandEvent;
import com.elypia.commandler.parsing.IParamParser;

public class StringParser implements IParamParser<String> {

    @Override
    public String parse(CommandEvent event, SearchScope scope, String input) {
        return input;
    }
}
