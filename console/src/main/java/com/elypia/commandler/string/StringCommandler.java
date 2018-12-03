package com.elypia.commandler.string;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.parsers.NumberParser;
import com.elypia.commandler.string.builders.*;
import com.elypia.commandler.string.client.*;
import org.slf4j.*;

public class StringCommandler extends Commandler<StringClient, StringEvent, String> {

    private static final Logger logger = LoggerFactory.getLogger(StringCommandler.class);

    public StringCommandler(StringClient client, String... prefixes) {
        // ? Initialise Commandler
        super(new StringConfiler(prefixes));

        this.client = client;
        dispatcher = new StringDispatcher(this);

        // ? Register any builders we have with the builder
        registerBuilder(new DefaultBuilder(), String.class);
        registerBuilder(new NumberBuilder(), NumberParser.TYPES);
    }

    @Override
    public String trigger(StringEvent event, String input, boolean send) {
        return dispatcher.processEvent(event, input, send);
    }
}
