package com.elypia.commandler.adapters;

import com.elypia.commandler.api.Adapter;
import com.elypia.commandler.event.ActionEvent;
import com.elypia.commandler.metadata.MetaParam;

import javax.inject.Singleton;
import java.net.*;

@Singleton
public class UrlAdapter implements Adapter<URL> {

    @Override
    public URL adapt(String input, Class<? extends URL> type, MetaParam data, ActionEvent<?, ?> event) {
        try {
            return new URL(input);
        } catch (MalformedURLException e) {
            return null;
        }
    }
}
