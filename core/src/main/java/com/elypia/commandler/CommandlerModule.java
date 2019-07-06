package com.elypia.commandler;

import com.elypia.commandler.interfaces.*;
import com.elypia.commandler.managers.*;
import com.google.inject.*;

import java.text.NumberFormat;

public class CommandlerModule extends AbstractModule {

    private final Commandler commandler;

    public CommandlerModule(final Commandler commandler) {
        this.commandler = commandler;
    }

    @Override
    protected void configure() {
        bind(Commandler.class).toInstance(commandler);
        bind(MisuseHandler.class).toInstance(commandler.getMisuseManager());
        bind(LanguageInterface.class).toInstance(commandler.getLanguageManager());
        bind(ResponseManager.class).toInstance(commandler.getResponseManager());
        bind(DispatcherManager.class).toInstance(commandler.getDispatcherManager());
        bind(ValidationManager.class).toInstance(commandler.getValidationManager());
        bind(AdapterManager.class).toInstance(commandler.getAdapterManager());
        bind(TestManager.class).toInstance(commandler.getTestManager());
        bind(Injector.class).toInstance(commandler.getInjector());

        // TODO: This shouldn't be configured by Commandler itself.
        bind(NumberFormat.class).toInstance(NumberFormat.getInstance());
    }
}
