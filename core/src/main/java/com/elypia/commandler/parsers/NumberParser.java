package com.elypia.commandler.parsers;

import com.elypia.commandler.CommandEvent;
import com.elypia.commandler.impl.IParser;

public class NumberParser implements IParser<Number> {

    @Override
    public Number parse(CommandEvent event, Class<? extends Number> type, String input) {
        try {
            if (type == Double.class)
                return Double.parseDouble(input);
            if (type == Integer.class)
                return Integer.parseInt(input);
        } catch (NumberFormatException ex) {
            return null;
        }

        return null;
    }
}
