package com.elypia.commandler.impl;

import com.elypia.commandler.Commandler;

public class TestEvent extends CommandEvent<Void, String, String> {

    public TestEvent(Commandler<Void, String, String> commandler, CommandInput input) {
        super(commandler, input);
    }
}
