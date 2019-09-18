package org.elypia.commandler.adapters;

import org.elypia.commandler.api.Adapter;
import org.elypia.commandler.event.ActionEvent;
import org.elypia.commandler.metadata.MetaParam;

import javax.inject.Singleton;

@Singleton
public class StringAdapter implements Adapter<CharSequence> {

    @Override
    public CharSequence adapt(String input, Class<? extends CharSequence> type, MetaParam data, ActionEvent<?, ?> event) {
        return input;
    }
}
