package com.elypia.commandler.parsing.parsers;

import com.elypia.commandler.events.CommandEvent;
import com.elypia.commandler.impl.IParser;

public class NumberParser implements IParser<Number> {

    @Override
    public Number parse(CommandEvent event, String input) {
        try {
            double value = Double.parseDouble(input);
            return Double.isInfinite(value) ? null : value;
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
