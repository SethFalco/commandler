package com.elypia.commandler.parsing.parsers;

import com.elypia.commandler.data.SearchScope;
import com.elypia.commandler.events.CommandEvent;
import com.elypia.commandler.parsing.IParamParser;

public class LongParser implements IParamParser<Long> {

    @Override
    public Long parse(CommandEvent event, SearchScope scope, String input) {
        try {
            return Long.parseLong(input);
        } catch (NumberFormatException ex){
            event.invalidate("Parameter `" + input + "` is not a number.");
            return null;
        }
    }
}
