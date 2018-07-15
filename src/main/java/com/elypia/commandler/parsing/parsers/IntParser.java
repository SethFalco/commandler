package com.elypia.commandler.parsing.parsers;

import com.elypia.commandler.data.SearchScope;
import com.elypia.commandler.events.CommandEvent;
import com.elypia.commandler.impl.IParamParser;

public class IntParser implements IParamParser<Integer> {

    @Override
    public Integer parse(CommandEvent event, SearchScope scope, String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
