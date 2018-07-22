package com.elypia.commandler.parsers;

import com.elypia.commandler.CommandEvent;
import com.elypia.commandler.impl.IParser;
import org.apache.commons.lang3.math.NumberUtils;

public class CharacterParser implements IParser<Character> {

    public static final Class[] TYPES = {
        Character.class, char.class
    };

    @Override
    public Character parse(CommandEvent event, Class<? extends Character> type, String input) {
        if (input.length() == 1)
            return input.charAt(0);

        if (NumberUtils.isDigits(input))
            return (char)Integer.parseInt(input);

        return null;
    }
}
