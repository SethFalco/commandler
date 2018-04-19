package com.elypia.commandler.parsing.parsers;

import com.elypia.commandler.parsing.impl.IParser;

public class LongParser implements IParser<Long> {

    @Override
    public Long parse(String input) throws IllegalArgumentException {
        try {
            return Long.parseLong(input);
        } catch (NumberFormatException ex){
            throw new IllegalArgumentException("Parameter `" + input + "` is not a number.");
        }
    }
}
