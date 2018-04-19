package com.elypia.commandler.parsing.parsers.java;

import com.elypia.commandler.parsing.impl.IParser;

public class StringParser implements IParser<String> {

    @Override
    public String parse(String input) throws IllegalArgumentException {
        return input;
    }
}
