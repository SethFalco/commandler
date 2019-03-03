package com.elypia.commandler.inject;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.interfaces.*;
import com.google.inject.AbstractModule;

public class CommandlerModule extends AbstractModule {

    private Commandler commandler;

    public CommandlerModule(Commandler commandler) {
        this.commandler = commandler;
    }

    @Override
    protected void configure() {
        bind(Commandler.class).toInstance(commandler);
        bind(LanguageAdapter.class).toInstance(commandler.getEngine());
        bind(MisuseListener.class).toInstance(commandler.getMisuseHandler());
    }
}
