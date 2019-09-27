/*
 * Copyright 2019-2019 Elypia CIC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.elypia.commandler.managers;

import com.google.inject.Module;
import com.google.inject.*;

import javax.inject.Singleton;
import java.util.*;

/**
 * Small abstraction of the dependency injection library in use.
 * This centralised some of the code, and makes it easier to allow
 * various components to add more with interupting eachother.
 *
 * @author seth@elypia.org (Syed Shah)
 */
@Singleton
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
     * It's much preferred to use {@link #add(Module...)}
     * unless only adding a single class.
     *
     * @param instance The instance of an object to add.
     * @param type The type of this object.
     * @param <T>
     */
    public <T> void add(T instance, Class<T> type) {
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
