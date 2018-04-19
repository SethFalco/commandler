package com.elypia.commandler.parsing.parsers;

import com.elypia.commandler.parsing.impl.IParser;

public class BooleanParser implements IParser<Boolean> {

    private static final String[] BOOLEAN = {"true", "yes", "1", "✔"};

    @Override
    public Boolean parse(String input) {
        for (String bool : BOOLEAN) {
            if (bool.equalsIgnoreCase(input))
                return true;
        }

        return false;
    }
}
