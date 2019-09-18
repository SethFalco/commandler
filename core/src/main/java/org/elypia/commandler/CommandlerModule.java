package org.elypia.commandler;

import com.google.inject.AbstractModule;

import java.text.NumberFormat;

public class CommandlerModule extends AbstractModule {

    private final Commandler commandler;

    public CommandlerModule(final Commandler commandler) {
        this.commandler = commandler;
    }

    @Override
    protected void configure() {
        bind(Commandler.class).toInstance(commandler);

        // TODO: This shouldn't be configured by Commandler itself.
        bind(NumberFormat.class).toInstance(NumberFormat.getInstance());
    }
}
