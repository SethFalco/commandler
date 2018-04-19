package com.elypia.commandler.parsing.parsers;

import com.elypia.commandler.parsing.impl.IParser;

public class IntParser implements IParser<Integer> {

    @Override
    public Integer parse(String input) throws IllegalArgumentException {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Parameter `" + input + "` is not a number.");
        }
    }
}
