package com.elypia.commandler.impl;

import com.elypia.commandler.Commandler;

/**
 * Parsers are what allow {@link Commandler} to know how
 * to interpret an object from the text a chat service provides us.
 * Due to chat services being comprised of just text we need
 * a {@link IParser parser} for each data type we're able or want
 * to be compatible with, and this will allow us to use that data type
 * as a parameter in commands.
 *
 * @param <O> The type of data we want to parse our input as.
 */
public interface IParser<CE extends ICommandEvent, O> {

    /**
     * This method should parse our input provides
     * which is a single parameter into the desired data type.
     *
     * @param event The parent event this parameter was used on.
     * @param type The type of object we're trying to build,
     *             this may not be the same as the parameterised type
     *             {@link O} if this is assignable from the type.
     * @param input The input from the user.
     * @return The parsed data-type, or null if we're unable to parse the input.
     */
    O parse(CE event, Class<? extends O> type, String input);
}
