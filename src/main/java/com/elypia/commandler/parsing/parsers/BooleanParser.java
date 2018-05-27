package com.elypia.commandler.parsing.parsers;

import com.elypia.commandler.data.SearchScope;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.parsing.impl.JDAParser;

public class BooleanParser extends JDAParser<Boolean> {

    private static final String[] BOOLEAN = {"true", "yes", "1", "✔"};

    @Override
    public Boolean parse(MessageEvent event, SearchScope scope, String input) {
        for (String bool : BOOLEAN) {
            if (bool.equalsIgnoreCase(input))
                return true;
        }

        return false;
    }
}
