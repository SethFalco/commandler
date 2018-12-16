package com.elypia.commandler.parsers;

import com.elypia.commandler.annotations.Compatible;
import com.elypia.commandler.interfaces.*;
import com.elypia.commandler.metadata.ParamData;

import java.util.*;

@Compatible({Boolean.class, boolean.class})
public class BooleanParser implements IParser<ICommandEvent, Boolean> {

    private static final Collection<String> TRUE = List.of(
        "true", "t", "yes", "y", "1", "one", "✔"
    );

    private static final Collection<String> FALSE = List.of(
        "false", "f", "no", "n", "0", "zero", "❌"
    );

    @Override
    public Boolean parse(ICommandEvent event, ParamData data, Class<? extends Boolean> type, String input) {
        input = input.toLowerCase();

        if (TRUE.contains(input))
            return true;

        if (FALSE.contains(input))
            return false;

        return null;
    }
}
