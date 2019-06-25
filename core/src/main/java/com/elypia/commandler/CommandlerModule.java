package com.elypia.commandler;

import com.google.inject.AbstractModule;

import java.text.NumberFormat;

public class CommandlerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(NumberFormat.class).toInstance(NumberFormat.getInstance());
    }
}
