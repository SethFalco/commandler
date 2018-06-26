package com.elypia.commandler.parsing.parsers;

import com.elypia.commandler.data.SearchScope;
import com.elypia.commandler.events.CommandEvent;
import com.elypia.commandler.parsing.IParamParser;

import java.net.*;

public class UrlParser implements IParamParser<URL> {

    @Override
    public URL parse(CommandEvent event, SearchScope scope, String input) {
        try {
            return new URL(input);
        } catch (MalformedURLException e) {
            event.invalidate("Parameter `" + input + "` is not a valid URL.");
            return null;
        }
    }
}
