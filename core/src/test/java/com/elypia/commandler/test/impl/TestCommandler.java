package com.elypia.commandler.test.impl;

import com.elypia.commandler.*;

public class TestCommandler extends Commandler<String, String> {

    protected TestCommandler(CommandlerBuilder<?, String, String> commandlerBuilder) {
        super(commandlerBuilder);
    }

    public String execute(String event) {
        return super.execute(event, event, false);
    }
}
