package com.elypia.commandler.parsing;

import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.parsing.impl.IParser;
import com.elypia.commandler.parsing.parsers.elypiai.OsuModeParser;
import com.elypia.commandler.parsing.parsers.java.*;
import com.elypia.elypiai.osu.data.OsuMode;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ParamParser {

    private Map<Class<?>, IParser> parsers;

    public ParamParser() {
        parsers = new HashMap<>();

        registerParser(OsuMode.class, new OsuModeParser());

        registerParser(boolean.class, new BooleanParser());
        registerParser(double.class, new DoubleParser());
        registerParser(int.class, new IntParser());
        registerParser(long.class, new LongParser());
        registerParser(URL.class, new UrlParser());
    }

    public <T> void registerParser(Class<T> t, Parser<T> parser) {
        if (parsers.keySet().contains(t))
            throw new IllegalArgumentException("Parser for this type of object has already been registered.");

        parsers.put(t, parser);
    }

    public static Object parseParam(MessageEvent event, Object object, Class<?> clazz) throws IllegalArgumentException {
        boolean array = object.getClass().isArray();

        if (clazz.isArray()) {
            String[] input = array ? (String[])object : new String[] {(String)object};

            // temp
        } else {
            if (array)
                throw new IllegalArgumentException("Parameter `" + String.join(", ", object + "` can't be a list.");

            String input = (String)object;

            // temp
        }

        throw new IllegalArgumentException("Sorry, this command was made incorrectly.");
    }
}
