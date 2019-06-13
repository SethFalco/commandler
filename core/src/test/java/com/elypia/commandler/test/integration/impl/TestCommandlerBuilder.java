package com.elypia.commandler.test.integration.impl;

public class TestCommandlerBuilder extends CommandlerBuilder<TestCommandlerBuilder, String, String> {

    @Override
    public TestCommandler build() {
        initializeDefaults();
        return new TestCommandler(this);
    }
}
