package com.elypia.commandler.parsing.parsers;

import com.elypia.commandler.parsing.impl.IParser;

import java.net.*;

public class UrlParser implements IParser<URL> {

    @Override
    public URL parse(String input) throws IllegalArgumentException {
        try {
            return new URL(input);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Parameter `" + input + "` is not a valid URL.");
        }
    }
}
