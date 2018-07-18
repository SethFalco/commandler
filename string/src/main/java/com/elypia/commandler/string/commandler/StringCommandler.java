package com.elypia.commandler.string.commandler;

import com.elypia.commandler.*;
import com.elypia.commandler.string.commandler.builders.DefaultBuilder;
import com.elypia.commandler.string.client.*;

public class StringCommandler extends Commandler<StringClient, StringEvent, String> {

    public StringCommandler(StringClient client, String... prefixes) {
        super(new Confiler<>(prefixes));
        this.client = client;
        dispatcher = new StringDispatcher(this);
        registerBuilder(String.class, new DefaultBuilder());
    }

    @Override
    public String trigger(StringEvent event, String input) {
        return dispatcher.processEvent(event, input);
    }
}
