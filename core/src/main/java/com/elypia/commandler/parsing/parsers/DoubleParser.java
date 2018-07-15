package com.elypia.commandler.parsing.parsers;

import com.elypia.commandler.events.CommandEvent;
import com.elypia.commandler.impl.IParser;

public class DoubleParser implements IParser<Double> {

    @Override
    public Double parse(CommandEvent event, String input) {
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException ex){
            return null;
        }
    }
}
