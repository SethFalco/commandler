package com.elypia.commandler.managers;

import com.google.inject.Module;
import com.google.inject.*;

import java.util.*;

/**
 * Small abstraction of the dependency injection library in use.
 * This centralised some of the code, and makes it easier to allow
 * various components to add more with interupting eachother.
 */
public class InjectionManager {

    private Injector injector;

    public InjectionManager(Module... modules) {
        this(List.of(modules));
    }

    public InjectionManager(Collection<Module> modules) {
        this(Stage.PRODUCTION, modules);
    }

    public InjectionManager(Stage stage, Collection<Module> modules) {
        this(Guice.createInjector(stage, modules));
    }

    public InjectionManager(Injector injector) {
        this.injector = injector;
    }

    public void add(Module... modules) {
        injector = injector.createChildInjector(modules);
    }

    public void add(Collection<Module> modules) {
        injector = injector.createChildInjector(modules);
    }

    /**
     * It's much prefferred to use {@link #add(Module...)}
     * unless only adding a single class.
     *
     * @param instance The instance of an object to add.
     * @param type The type of this object.
     * @param <T>
     */
    public <T> void addInstance(T instance, Class<T> type) {
        add(new AbstractModule() {

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
