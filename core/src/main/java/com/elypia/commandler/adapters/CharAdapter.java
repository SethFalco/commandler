package com.elypia.commandler.adapters;

import com.elypia.commandler.CommandlerEvent;
import com.elypia.commandler.annotations.Adapter;
import com.elypia.commandler.interfaces.ParamAdapter;
import com.elypia.commandler.metadata.data.MetaParam;

import javax.inject.Singleton;

@Singleton
@Adapter({Character.class, char.class})
public class CharAdapter implements ParamAdapter<Character> {

    @Override
    public Character adapt(String input, Class<? extends Character> type, MetaParam data, CommandlerEvent<?> event) {
        if (input.length() == 1)
            return input.charAt(0);

        if (input.length() < 4) {
            try {
                return (char)Byte.parseByte(input);
            } catch (NumberFormatException ex) {
                return null;
            }
        }

        return null;
    }
}
