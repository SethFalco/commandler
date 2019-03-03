package com.elypia.commandler.inject;

import com.elypia.commandler.Commandler;
import com.google.inject.Module;
import com.google.inject.*;

import java.util.*;

/**
 * The ServiceProvider uses {@link Guice} to use
 * dependency injection to get dependencies to your
 * Modules.
 *
 * Comandler is using Guice as it appears to have the simplist
 * interface while following standard conventions for javax.inject.
 */
public class ServiceProvider {

    /**
     * The injector which is made from all modules that are added.
     */
    private Injector injector;
    private Collection<Module> modules;

    public ServiceProvider(Commandler commandler) {
        modules = new ArrayList<>();
        modules.add(new CommandlerModule(commandler));
    }

    public <T> T get(Class<T> clazz) {
        return injector.getInstance(clazz);
    }

    public void createInjector() {
        if (injector != null)
            throw new IllegalStateException("Can't create injector multiple times.");

        injector = Guice.createInjector(modules);
    }
}
