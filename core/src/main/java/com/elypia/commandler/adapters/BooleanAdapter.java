package com.elypia.commandler.adapters;

import com.elypia.commandler.annotations.Compatible;
import com.elypia.commandler.interfaces.Adapter;
import com.elypia.commandler.metadata.data.ParamData;

import javax.inject.Singleton;
import java.util.*;

@Singleton
@Compatible({Boolean.class, boolean.class})
public class BooleanAdapter implements Adapter<Boolean> {

    private static final Collection<String> TRUE = List.of(
        // Formal responses
        "true", "t",

        // Developer responses
        "1", "one",

        // Normal responses
        "yes", "y",

        // Informal responses
        "ya", "ye", "yea", "yeah",

        // Characters
        "✔"
    );

    private static final Collection<String> FALSE = List.of(
        // Formal responses
        "false", "f",

        // Developer responses
        "0", "zero",

        // Normal responses
        "no", "n",

        // Informal responses
        "nah", "nope",

        // Characters
        "❌"
    );

    @Override
    public Boolean adapt(String input, Class<? extends Boolean> type, ParamData data) {
        input = input.toLowerCase();

        if (TRUE.contains(input))
            return true;

        if (FALSE.contains(input))
            return false;

        return null;
    }
}
