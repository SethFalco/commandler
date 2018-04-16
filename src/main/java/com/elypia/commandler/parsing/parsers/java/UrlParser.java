package com.elypia.commandler.parsing.parsers.java;

import com.elypia.commandler.parsing.impl.Parser;

import java.net.MalformedURLException;
import java.net.URL;

public class UrlParser implements Parser<URL> {

    @Override
    public URL parse(String input) throws IllegalArgumentException {
        try {
            return new URL(input);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Parameter `" + input + "` is not a valid URL.");
        }
    }
}
