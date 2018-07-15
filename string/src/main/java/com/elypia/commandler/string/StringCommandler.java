package com.elypia.commandler.string;

import com.elypia.commandler.Commandler;

public class StringCommandler extends Commandler<Void, Void, String> {

    public static final String EVENT_PROVIDED = "The StringCommandler does not have a client or events.";

    public StringCommandler(String... prefixes) {
        super(prefixes);
    }

    @Override
    public String trigger(Void event, String input) {
        throw new UnsupportedOperationException(EVENT_PROVIDED);
    }

    public String trigger(String input) {
        return dispatcher.processEvent(null, input);
    }
}
