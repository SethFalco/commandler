package com.elypia.commandler.string;

import com.elypia.commandler.*;
import com.elypia.commandler.parsers.NumberParser;
import com.elypia.commandler.string.builders.*;
import com.elypia.commandler.string.client.*;
import org.slf4j.*;

public class StringCommandler extends Commandler<StringClient, StringEvent, String> {

    private static final Logger logger = LoggerFactory.getLogger(StringCommandler.class);

    public StringCommandler(StringClient client, String... prefixes) {
        // ? Initialise Commandler
        super(new Confiler<>(prefixes) {
            @Override
            public CommandEvent<StringClient, StringEvent, String> processEvent(Commandler<StringClient, StringEvent, String> commandler, StringEvent event, String content) {
                var parent = super.processEvent(commandler, event, content);
                return new StringCommand(parent);
            }
        });

        this.client = client;
        dispatcher = new StringDispatcher(this);

        // ? Register any builders we have with the builder
        registerBuilder(new DefaultBuilder(), String.class);
        registerBuilder(new NumberBuilder(), NumberParser.TYPES);

        logger.info("New instance of StringCommandler succesfully initialised.");
    }

    @Override
    public String trigger(StringEvent event, String input) {
        return dispatcher.processEvent(event, input);
    }
}
