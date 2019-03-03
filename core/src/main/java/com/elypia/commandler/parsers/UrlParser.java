package com.elypia.commandler.parsers;

import com.elypia.commandler.annotations.Compatible;
import com.elypia.commandler.interfaces.*;
import com.elypia.commandler.metadata.data.ParamData;

import java.net.*;

@Compatible(URL.class)
public class UrlParser implements Parser<CommandlerEvent, URL> {

    @Override
    public URL parse(CommandlerEvent event, ParamData data, Class<? extends URL> type, String input) {
        try {
            return new URL(input);
        } catch (MalformedURLException e) {
            return null;
        }
    }
}
