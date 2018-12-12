package com.elypia.commandler.parsers;

import com.elypia.commandler.annotations.Compatible;
import com.elypia.commandler.interfaces.*;
import org.apache.commons.lang3.math.NumberUtils;

@Compatible({Character.class, char.class})
public class CharacterParser implements IParser<ICommandEvent, Character> {

    @Override
    public Character parse(ICommandEvent event, Class<? extends Character> type, String input) {
        if (input.length() == 1)
            return input.charAt(0);

        if (NumberUtils.isDigits(input))
            return (char)Integer.parseInt(input);

        return null;
    }
}
