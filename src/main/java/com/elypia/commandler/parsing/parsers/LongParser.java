package com.elypia.commandler.parsing.parsers;

import com.elypia.commandler.data.SearchScope;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.parsing.impl.JDAParser;

public class LongParser extends JDAParser<Long> {

    @Override
    public Long parse(MessageEvent event, SearchScope scope, String input) throws IllegalArgumentException {
        try {
            return Long.parseLong(input);
        } catch (NumberFormatException ex){
            throw new IllegalArgumentException("Parameter `" + input + "` is not a number.");
        }
    }
}
