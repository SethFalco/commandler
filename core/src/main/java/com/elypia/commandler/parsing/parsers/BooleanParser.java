package com.elypia.commandler.parsing.parsers;

import com.elypia.commandler.events.CommandEvent;
import com.elypia.commandler.impl.IParser;

public class BooleanParser implements IParser<Boolean> {

    private static final String[] TRUE = {
        "true", "yes", "y", "1", "✔"
    };

    private static final String[] FALSE = {
        "false", "no", "n", "0", "❌"
    };

    @Override
    public Boolean parse(CommandEvent event, String input) {
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
