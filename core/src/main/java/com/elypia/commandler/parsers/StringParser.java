package com.elypia.commandler.parsers;

import com.elypia.commandler.impl.*;

public class StringParser implements IParser<ICommandEvent, String> {

    @Override
    public String parse(ICommandEvent event, Class<? extends String> type, String input) {
        return input;
    }
}
