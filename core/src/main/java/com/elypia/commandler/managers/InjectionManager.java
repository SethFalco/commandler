package com.elypia.commandler.managers;

import com.google.inject.Module;
import com.google.inject.*;

/**
 * Small abstraction of the dependency injection library in use.
 * This centralised some of the code, and makes it easier to allow
 * various components to add more with interupting eachother.
 */
public class InjectionManager {

    private Injector injector;

    public InjectionManager() {
        this(Guice.createInjector());
    }

    public InjectionManager(Injector injector) {
        this.injector = injector;
    }

    public void addModules(Module... modules) {
        injector = injector.createChildInjector(modules);
    }

    /**
     * It's much prefferred to use {@link #addModules(Module...)}
     * unless only adding a single class.
     *
     * @param instance The instance of an object to add.
     * @param type The type of this object.
     * @param <T>
     */
    public <T> void addInstance(T instance, Class<T> type) {
        addModules(new AbstractModule() {

            @Override
            protected void configure() {
                bind(type).toInstance(instance);
            }
        });
    }

    public Injector getInjector() {
        return injector;
    }

    public void setInjector(Injector injector) {
        this.injector = injector;
    }
}
