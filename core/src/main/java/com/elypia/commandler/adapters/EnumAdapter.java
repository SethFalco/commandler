package com.elypia.commandler.adapters;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.annotations.Compatible;
import com.elypia.commandler.interfaces.Adapter;
import com.elypia.commandler.metadata.data.MetaParam;

import javax.inject.Singleton;
import java.util.Objects;

/**
 * This is the generic {@link Enum} parser. This should be the fallback parser
 * for any enums that are registered to {@link Commandler} if a
 * {@link Adapter} of that type was not specifically registered. <br>
 * This simply checks if the names are the same after removing
 * spaces, under scores, and converting to lower case.
 */
@Singleton
@Compatible(Enum.class)
public class EnumAdapter implements Adapter<Enum> {

    @Override
    public Enum adapt(String input, Class<? extends Enum> type, MetaParam data) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(input);
        input = input.toLowerCase().replace(" ", "");

        for (Enum e : type.getEnumConstants()) {
            String name = e.name().toLowerCase().replace("_", "");

            if (name.equals(input))
                return e;
        }

        return null;
    }
}
