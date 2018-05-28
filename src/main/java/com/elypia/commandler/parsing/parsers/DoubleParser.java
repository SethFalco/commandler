package com.elypia.commandler.parsing.parsers;

import com.elypia.commandler.data.SearchScope;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.parsing.ParamParser;

public class DoubleParser implements ParamParser<Double> {

    @Override
    public Double parse(MessageEvent event, SearchScope scope, String input) throws IllegalArgumentException {
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException ex){
            throw new IllegalArgumentException("Parameter `" + input + "` is not a number.");
        }
    }
}
