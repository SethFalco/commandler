package com.elypia.commandler.string;

import com.elypia.commandler.*;
import com.elypia.commandler.parsers.NumberParser;
import com.elypia.commandler.string.builders.*;
import com.elypia.commandler.string.client.*;

public class StringCommandler extends Commandler<StringClient, StringEvent, String> {

    public StringCommandler(StringClient client, String... prefixes) {
        super(new Confiler<>(prefixes));
        this.client = client;
        dispatcher = new StringDispatcher(this);

        registerBuilder(new DefaultBuilder(), String.class);
        registerBuilder(new NumberBuilder(), NumberParser.TYPES);
    }

    @Override
    public String trigger(StringEvent event, String input) {
        return dispatcher.processEvent(event, input);
    }
}
