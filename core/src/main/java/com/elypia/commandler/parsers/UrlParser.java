package com.elypia.commandler.parsers;

import com.elypia.commandler.annotations.Compatible;
import com.elypia.commandler.interfaces.*;
import com.elypia.commandler.metadata.ParamData;

import java.net.*;

@Compatible(URL.class)
public class UrlParser implements IParser<ICommandEvent, URL> {

    @Override
    public URL parse(ICommandEvent event, ParamData data, Class<? extends URL> type, String input) {
        try {
            return new URL(input);
        } catch (MalformedURLException e) {
            return null;
        }
    }
}
