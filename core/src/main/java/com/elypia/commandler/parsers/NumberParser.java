package com.elypia.commandler.parsers;

import com.elypia.commandler.CommandEvent;
import com.elypia.commandler.impl.IParser;

public class NumberParser implements IParser<Number> {

    @Override
    public Number parse(CommandEvent event, Class<? extends Number> type, String input) {
        try {
            if (type == Double.class || type == double.class)
                return Double.parseDouble(input);
            if (type == Float.class || type == float.class)
                return Float.parseFloat(input);

            if (type == Long.class || type == long.class)
                return Long.parseLong(input);
            if (type == Integer.class || type == int.class)
                return Integer.parseInt(input);
            if (type == Short.class || type == short.class)
                return Short.parseShort(input);
            if (type == Byte.class || type == byte.class)
                return Byte.parseByte(input);
        } catch (NumberFormatException ex) {
            return null;
        }

        return null;
    }
}
