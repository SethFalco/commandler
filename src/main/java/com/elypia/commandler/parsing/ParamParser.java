package com.elypia.commandler.parsing;

import com.elypia.commandler.parsing.impl.IParser;
import com.elypia.commandler.parsing.parsers.*;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ParamParser {

    protected Map<Class<?>, IParser> parsers;

    public ParamParser() {
        parsers = new HashMap<>();

        registerParser(boolean.class, new BooleanParser());
        registerParser(double.class, new DoubleParser());
        registerParser(int.class, new IntParser());
        registerParser(long.class, new LongParser());
        registerParser(String.class, new StringParser());
        registerParser(URL.class, new UrlParser());
    }

    public <T> void registerParser(Class<T> t, IParser<T> parser) {
        if (parsers.keySet().contains(t))
            throw new IllegalArgumentException("Parser for this type of object has already been registered.");

        parsers.put(t, parser);
    }

    public Object parseParam(Object object, Class<?> clazz) throws IllegalArgumentException {
        boolean array = object.getClass().isArray();

        if (clazz.isArray()) {
            String[] input = array ? (String[])object : new String[] {(String)object};
            Object[] objects = new Object[input.length];

            for (int i = 0; i < input.length; i++)
                objects[i] = parsers.get(clazz.getComponentType()).parse(input[i]);

            return objects;
        } else {
            if (array)
                throw new IllegalArgumentException("Parameter `" + String.join(", ", object + "` can't be a list."));

            String input = (String)object;

            return parsers.get(clazz).parse(input);
        }
    }
}
