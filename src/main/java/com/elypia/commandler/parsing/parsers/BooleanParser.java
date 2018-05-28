package com.elypia.commandler.parsing.parsers;

import com.elypia.commandler.data.SearchScope;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.parsing.ParamParser;

public class BooleanParser implements ParamParser<Boolean> {

    private static final String[] BOOLEAN = {
        "true",
        "yes",
        "1",
        "✔",
        "<:tickYes:315009125694177281>", // Added these here anyways
        "<:greentick:396521773245530123>" // Probably rarely used if ever
    };

    @Override
    public Boolean parse(MessageEvent event, SearchScope scope, String input) throws IllegalArgumentException {
        for (String bool : BOOLEAN) {
            if (bool.equalsIgnoreCase(input))
                return true;
        }

        return false;
    }
}
