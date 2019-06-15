package com.elypia.commandler;

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
        bind(LanguageManager.class).toInstance(commandler.getEngine());
        bind(MisuseHandler.class).toInstance(commandler.getMisuseHandler());
    }
}
