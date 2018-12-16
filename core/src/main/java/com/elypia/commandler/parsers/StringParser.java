package com.elypia.commandler.parsers;

import com.elypia.commandler.annotations.Compatible;
import com.elypia.commandler.interfaces.*;
import com.elypia.commandler.metadata.ParamData;

@Compatible(String.class)
public class StringParser implements IParser<ICommandEvent, String> {

    @Override
    public String parse(ICommandEvent event, ParamData data, Class<? extends String> type, String input) {
        return input;
    }
}
