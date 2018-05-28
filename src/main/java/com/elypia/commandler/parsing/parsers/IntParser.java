package com.elypia.commandler.parsing.parsers;

import com.elypia.commandler.data.SearchScope;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.parsing.ParamParser;

public class IntParser implements ParamParser<Integer> {

    @Override
    public Integer parse(MessageEvent event, SearchScope scope, String input) throws IllegalArgumentException {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Parameter `" + input + "` is not a number.");
        }
    }
}
