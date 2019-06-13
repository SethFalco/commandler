package com.elypia.commandler.test.integration.impl;

import com.elypia.commandler.Commandler;

public class TestCommandler extends Commandler<String, String> {

    protected TestCommandler(CommandlerBuilder<?, String, String> commandlerBuilder) {
        super(commandlerBuilder);
    }

    public String execute(String event) {
        return super.execute(event, event, false);
    }
}
