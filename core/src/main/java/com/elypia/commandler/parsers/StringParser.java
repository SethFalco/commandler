package com.elypia.commandler.parsers;

import com.elypia.commandler.CommandEvent;
import com.elypia.commandler.impl.IParser;

import java.lang.reflect.Type;
import java.time.Duration;

public class StringParser implements IParser<String> {

    @Override
    public String parse(CommandEvent event, Class<? extends String> type, String input) {
        return input;
    }
}
