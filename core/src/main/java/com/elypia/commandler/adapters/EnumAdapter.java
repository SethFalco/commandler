package com.elypia.commandler.adapters;

import com.elypia.commandler.*;
import com.elypia.commandler.annotations.Adapter;
import com.elypia.commandler.interfaces.ParamAdapter;
import com.elypia.commandler.metadata.MetaParam;

import javax.inject.Singleton;
import java.util.Objects;

/**
 * This is the generic {@link Enum} parser. This should be the fallback parser
 * for any enums that are registered to {@link Commandler} if a
 * {@link ParamAdapter} of that type was not specifically registered. <br>
 * This simply checks if the names are the same after removing
 * spaces, under scores, and converting to lower case.
 */
@Singleton
@Adapter(Enum.class)
public class EnumAdapter implements ParamAdapter<Enum> {

    @Override
    public Enum adapt(String input, Class<? extends Enum> type, MetaParam data, CommandlerEvent<?> event) {
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
