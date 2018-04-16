package com.elypia.commandler.parsing.parsers.java;

import com.elypia.commandler.parsing.impl.Parser;
import com.elypia.elypiai.utils.Regex;

public class IntParser implements Parser<Integer> {

    @Override
    public Integer parse(String input) throws IllegalArgumentException {
        if (!Regex.NUMBER.matches(input))
            throw new IllegalArgumentException("Parameter `" + input + "` is not a number.");

        return Integer.parseInt(input);
    }
}
