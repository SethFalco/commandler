package com.elypia.commandler.parsers;

import com.elypia.commandler.CommandEvent;
import com.elypia.commandler.impl.IParser;

import java.util.*;

public class BooleanParser implements IParser<Boolean> {

    private static final Collection<String> TRUE = Arrays.asList(
        "true", "yes", "y", "1", "✔"
    );

    private static final Collection<String> FALSE = Arrays.asList(
        "false", "no", "n", "0", "❌"
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
