package com.elypia.commandler.parsing.parsers.java;

import com.elypia.commandler.parsing.impl.Parser;

public class BooleanParser implements Parser<Boolean> {

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
