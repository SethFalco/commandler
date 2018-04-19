package com.elypia.commandler.parsing.parsers;

import com.elypia.commandler.parsing.impl.IParser;

public class DoubleParser implements IParser<Double> {

    @Override
    public Double parse(String input) throws IllegalArgumentException {
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException ex){
            throw new IllegalArgumentException("Parameter `" + input + "` is not a number.");
        }
    }
}
