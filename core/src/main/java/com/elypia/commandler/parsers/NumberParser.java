package com.elypia.commandler.parsers;

import com.elypia.commandler.annotations.Compatible;
import com.elypia.commandler.interfaces.*;
import com.elypia.commandler.metadata.ParamData;

@Compatible({Number.class, double.class, float.class, long.class, int.class, short.class, byte.class})
public class NumberParser implements IParser<ICommandEvent, Number> {

    @Override
    public Number parse(ICommandEvent event, ParamData data, Class<? extends Number> type, String input) {
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
