package com.elypia.commandler.adapters;

import com.elypia.commandler.api.Adapter;
import com.elypia.commandler.event.ActionEvent;
import com.elypia.commandler.metadata.MetaParam;

import javax.inject.Singleton;

@Singleton
public class CharAdapter implements Adapter<Character> {

    @Override
    public Character adapt(String input, Class<? extends Character> type, MetaParam data, ActionEvent<?, ?> event) {
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
