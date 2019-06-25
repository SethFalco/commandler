package com.elypia.commandler.interfaces;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.metadata.data.MetaParam;

/**
 * Parsers are what allow {@link Commandler} to know how
 * to interpret an object from the text a chat service provides us.
 * Due to chat injector being comprised of just text we need
 * a {@link ParamAdapter parser} for each data type we're able or want
 * to be compatible with, and this will allow us to use that data type
 * as a parameter in commands.
 *
 * @param <O> The type of data we want to adapt our input as.
 */
public interface ParamAdapter<O> {

    /**
     * This method should adapt our input provides
     * which is a single parameter into the desired data type.
     *
     * @param param The type of object we're trying to load,
     *             this may not be the same as the parameterised type
     *             {@link O} if this is assignable from the type.
     * @param input The input from the user.
     * @return The parsed data-type, or null if we're unable to adapt the input.
     */
    O adapt(String input, Class<? extends O> type, MetaParam param);

    default O adapt(String input) {
        return adapt(input, null);
    }

    default O adapt(String input, Class<? extends O> type) {
        return adapt(input, type, null);
    }
}
