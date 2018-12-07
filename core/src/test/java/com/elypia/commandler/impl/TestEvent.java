package com.elypia.commandler.impl;

import com.elypia.commandler.LanguageEngine;

public class TestEvent extends CommandEvent<Void, String, String> {

    public TestEvent(LanguageEngine<String> engine, CommandInput<Void, String, String> input) {
        super(engine, input);
    }
}
