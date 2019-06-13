package com.elypia.commandler.adapters;

import com.elypia.commandler.annotations.Compatible;
import com.elypia.commandler.interfaces.Adapter;
import com.elypia.commandler.metadata.data.ParamData;

import javax.inject.Singleton;

@Singleton
@Compatible({CharSequence.class, String.class})
public class StringAdapter implements Adapter<CharSequence> {

    @Override
    public CharSequence adapt(String input, Class<? extends CharSequence> type, ParamData data) {
        return input;
    }
}
