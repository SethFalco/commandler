package com.elypia.commandler.parsers;

import com.elypia.commandler.annotations.Compatible;
import com.elypia.commandler.interfaces.*;

import java.net.*;

@Compatible(URL.class)
public class UrlParser implements IParser<ICommandEvent, URL> {

    @Override
    public URL parse(ICommandEvent event, Class<? extends URL> type, String input) {
        try {
            return new URL(input);
        } catch (MalformedURLException e) {
            return null;
        }
    }
}
