package com.elypia.commandler.adapters;

import com.elypia.commandler.annotations.Compatible;
import com.elypia.commandler.interfaces.Adapter;
import com.elypia.commandler.meta.data.MetaParam;

import javax.inject.Singleton;
import java.net.*;

@Singleton
@Compatible(URL.class)
public class UrlAdapter implements Adapter<URL> {

    @Override
    public URL adapt(String input, Class<? extends URL> type, MetaParam data) {
        try {
            return new URL(input);
        } catch (MalformedURLException e) {
            return null;
        }
    }
}
