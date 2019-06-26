package com.elypia.commandler.adapters;

import com.elypia.commandler.CommandlerEvent;
import com.elypia.commandler.annotations.Adapter;
import com.elypia.commandler.interfaces.ParamAdapter;
import com.elypia.commandler.metadata.data.MetaParam;

import javax.inject.Singleton;

@Singleton
@Adapter({String.class})
public class StringAdapter implements ParamAdapter<CharSequence> {

    @Override
    public CharSequence adapt(String input, Class<? extends CharSequence> type, MetaParam data, CommandlerEvent<?> event) {
        return input;
    }
}
