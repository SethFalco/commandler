package com.elypia.commandler.parsers;

import com.elypia.commandler.annotations.Compatible;
import com.elypia.commandler.interfaces.*;
import com.elypia.commandler.metadata.data.ParamData;

@Compatible(String.class)
public class StringParser implements Parser<CommandlerEvent, String> {

    @Override
    public String parse(CommandlerEvent event, ParamData data, Class<? extends String> type, String input) {
        return input;
    }
}
