package com.elypia.commandler.parsing.parsers;

import com.elypia.commandler.data.SearchScope;
import com.elypia.commandler.events.CommandEvent;
import com.elypia.commandler.impl.IParamParser;

public class BooleanParser implements IParamParser<Boolean> {

    private static final String[] TRUE = {
        "true",
        "yes",
        "y",
        "1",
        "✔",
        "<:tickYes:315009125694177281>", // Added these here anyways
        "<:greentick:396521773245530123>" // Probably rarely used if ever
    };

    private static final String[] FALSE = {
        "false",
        "no",
        "n",
        "0",
        "❌",
        "<:tickNo:315009174163685377>",
        "<:redtick:396521773207912468>"
    };

    @Override
    public Boolean parse(CommandEvent event, SearchScope scope, String input) {
        for (String bool : TRUE) {
            if (bool.equalsIgnoreCase(input))
                return true;
        }

        for (String bool : FALSE) {
            if (bool.equalsIgnoreCase(input))
                return false;
        }

        return null;
    }
}
