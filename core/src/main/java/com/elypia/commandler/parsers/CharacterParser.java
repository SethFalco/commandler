package com.elypia.commandler.parsers;

import com.elypia.commandler.annotations.Compatible;
import com.elypia.commandler.interfaces.*;
import com.elypia.commandler.metadata.data.ParamData;

@Compatible({Character.class, char.class})
public class CharacterParser implements Parser<CommandlerEvent, Character> {

    private static final NumberParser number = new NumberParser();

    @Override
    public Character parse(CommandlerEvent event, ParamData data, Class<? extends Character> type, String input) {
        if (input.length() == 1)
            return input.charAt(0);

        if (number.parse(event, data, byte.class, input) != null)
            return (char)Integer.parseInt(input);

        return null;
    }
}
