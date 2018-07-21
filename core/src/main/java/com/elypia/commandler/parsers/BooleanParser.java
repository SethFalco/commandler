package com.elypia.commandler.parsers;

import com.elypia.commandler.CommandEvent;
import com.elypia.commandler.impl.IParser;

import java.util.*;

public class BooleanParser implements IParser<Boolean> {

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
    public Boolean parse(CommandEvent event, Class<? extends Boolean> type, String input) {
        input = input.toLowerCase();

        if (TRUE.contains(input))
            return true;

        if (FALSE.contains(input))
            return false;

        return null;
    }
}
