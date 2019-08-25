package com.elypia.commandler;

import com.elypia.commandler.managers.*;
import com.google.inject.AbstractModule;

public class DefaultModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(DispatcherManager.class).to(DispatcherManager.class);
        bind(InjectionManager.class).to(InjectionManager.class);
        bind(AdapterManager.class).to(AdapterManager.class);
        bind(TestManager.class).to(TestManager.class);
        bind(DispatcherManager.class).to(DispatcherManager.class);
        bind(ExceptionManager.class).to(ExceptionManager.class);
        bind(MessengerManager.class).to(MessengerManager.class);
    }
}
