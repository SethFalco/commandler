package com.elypia.commandler.parsers;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.impl.*;

/**
 * This is the generic {@link Enum} parser. This should be the fallback parser
 * for any enums that are registered to {@link Commandler} if a
 * {@link IParser} of that type was not specifically registered. <br>
 * This simply checks if the names are the same after removing
 * spaces, under scores, and converting to lower case.
 */

public class EnumParser implements IParser<ICommandEvent, Enum> {

    @Override
    public Enum parse(ICommandEvent event, Class<? extends Enum> type, String input) {
        input = input.toLowerCase().replace(" ", "");

        for (Enum e : type.getEnumConstants()) {
            String name = e.name().toLowerCase().replace("_", "");

            if (name.equals(input))
                return e;
        }

        return null;
    }
}
