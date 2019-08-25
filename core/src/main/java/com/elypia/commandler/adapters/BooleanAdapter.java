package com.elypia.commandler.adapters;

import com.elypia.commandler.api.Adapter;
import com.elypia.commandler.event.ActionEvent;
import com.elypia.commandler.metadata.MetaParam;

import javax.inject.Singleton;
import java.util.*;

@Singleton
public class BooleanAdapter implements Adapter<Boolean> {

    private static final Collection<String> TRUE = List.of(
        "true", "t", // Formal responses
        "1", "one", // Developer responses
        "yes", "y", // Normal responses
        "ya", "ye", "yea", "yeah", // Informal responses
        "✔" // Characters
    );

    private static final Collection<String> FALSE = List.of(
        "false", "f", // Formal responses
        "0", "zero", // Developer responses
        "no", "n", // Normal responses
        "nah", "nope", // Informal responses
        "❌" // Characters
    );

    @Override
    public Boolean adapt(String input, Class<? extends Boolean> type, MetaParam data, ActionEvent<?, ?> event) {
        input = input.toLowerCase();

        if (TRUE.contains(input))
            return true;

        if (FALSE.contains(input))
            return false;

        return null;
    }
}
