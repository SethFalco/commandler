package com.elypia.commandler.adapters;

import com.elypia.commandler.CommandlerEvent;
import com.elypia.commandler.annotations.Adapter;
import com.elypia.commandler.interfaces.ParamAdapter;
import com.elypia.commandler.metadata.data.MetaParam;

import javax.inject.Singleton;
import java.net.*;

@Singleton
@Adapter(URL.class)
public class UrlAdapter implements ParamAdapter<URL> {

    @Override
    public URL adapt(String input, Class<? extends URL> type, MetaParam data, CommandlerEvent<?> event) {
        try {
            return new URL(input);
        } catch (MalformedURLException e) {
            return null;
        }
    }
}
