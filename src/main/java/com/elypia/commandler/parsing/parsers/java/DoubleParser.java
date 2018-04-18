package com.elypia.commandler.parsing.parsers.java;

import com.elypia.commandler.parsing.impl.IParser;
import com.elypia.elypiai.utils.Regex;

public class DoubleParser implements IParser<Double> {

    @Override
    public Double parse(String input) throws IllegalArgumentException {
        if (!Regex.NUMBER.matches(input))
            throw new IllegalArgumentException("Parameter `" + input + "` is not a number.");

        return Double.parseDouble(input);
    }
}
