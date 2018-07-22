package com.elypia.commandler.parsers;

import com.elypia.commandler.impl.*;

import java.util.*;

public class BooleanParser implements IParser<ICommandEvent, Boolean> {

    public static final Class[] TYPES = {
        Boolean.class, boolean.class
    };

    private static final Collection<String> TRUE = List.of(
        "true", "t", "yes", "y", "1", "one", "✔"
    );

    private static final Collection<String> FALSE = List.of(
        "false", "f", "no", "n", "0", "zero", "❌"
    );

    @Override
    public Boolean parse(ICommandEvent event, Class<? extends Boolean> type, String input) {
        input = input.toLowerCase();

        if (TRUE.contains(input))
            return true;

        if (FALSE.contains(input))
            return false;

        return null;
    }
}
