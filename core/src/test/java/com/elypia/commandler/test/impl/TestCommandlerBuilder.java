package com.elypia.commandler.test.impl;

import com.elypia.commandler.CommandlerBuilder;

public class TestCommandlerBuilder extends CommandlerBuilder<TestCommandlerBuilder, String, String> {

    @Override
    public TestCommandler build() {
        initializeDefaults();
        return new TestCommandler(this);
    }
}