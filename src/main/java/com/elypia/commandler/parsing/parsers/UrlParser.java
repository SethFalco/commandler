package com.elypia.commandler.parsing.parsers;

import com.elypia.commandler.data.SearchScope;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.parsing.ParamParser;

import java.net.MalformedURLException;
import java.net.URL;

public class UrlParser implements ParamParser<URL> {

    @Override
    public URL parse(MessageEvent event, SearchScope scope, String input) throws IllegalArgumentException {
        try {
            return new URL(input);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Parameter `" + input + "` is not a valid URL.");
        }
    }
}
