package com.elypia.commandler.adapters;

import com.elypia.commandler.api.Adapter;
import com.elypia.commandler.event.ActionEvent;
import com.elypia.commandler.metadata.MetaParam;

import javax.inject.Singleton;

@Singleton
public class StringAdapter implements Adapter<CharSequence> {

    @Override
    public CharSequence adapt(String input, Class<? extends CharSequence> type, MetaParam data, ActionEvent<?, ?> event) {
        return input;
    }
}
