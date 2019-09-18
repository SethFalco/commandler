package org.elypia.commandler.adapters;

import org.elypia.commandler.api.Adapter;
import org.elypia.commandler.event.ActionEvent;
import org.elypia.commandler.metadata.MetaParam;

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
