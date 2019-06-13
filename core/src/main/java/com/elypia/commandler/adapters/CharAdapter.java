package com.elypia.commandler.adapters;

import com.elypia.commandler.annotations.Compatible;
import com.elypia.commandler.interfaces.Adapter;
import com.elypia.commandler.metadata.data.ParamData;

import javax.inject.Singleton;

@Singleton
@Compatible({Character.class, char.class})
public class CharAdapter implements Adapter<Character> {

    @Override
    public Character adapt(String input, Class<? extends Character> type, ParamData data) {
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
