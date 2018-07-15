package com.elypia.commandler.parsing.parsers;

import com.elypia.commandler.data.SearchScope;
import com.elypia.commandler.events.CommandEvent;
import com.elypia.commandler.impl.IParamParser;

public class DoubleParser implements IParamParser<Double> {

    @Override
    public Double parse(CommandEvent event, SearchScope scope, String input) {
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException ex){
            return null;
        }
    }
}
