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

package org.elypia.commandler.injection;

import com.google.inject.Module;
import com.google.inject.*;
import org.slf4j.*;

import javax.inject.Singleton;
import java.util.*;

/**
 * The injection service manages all runtime dependencies
 * and abstracts the chosen IoC framework in use for Commandler.
 *
 * This will be used to manage injection, add or remove injection
 * bindings in runtime, or tagging dependencies in order to get
 * lists of relevent injectable Java objects regardless of how
 * they are related.
 *
 * @author seth@elypia.org (Seth Falco)
 */
@Singleton
public class InjectorService {

    private static final Logger logger = LoggerFactory.getLogger(InjectorService.class);

    /**
     * The {@link Guice} injector, this is mutable as child injectors
     * may replace the parent injector during runtime.
     */
    private Injector injector;


    public InjectorService(Module... modules) {
        this(List.of(modules));
    }

    public InjectorService(Collection<Module> modules) {
        injector = Guice.createInjector(Stage.PRODUCTION, modules);
        logger.debug("Instantiated {} with {} bindings.", InjectorService.class.getSimpleName(), injector.getAllBindings().size());
    }

    /**
     * It's much preferred to use {@link #add(Module...)}.
     *
     * @param instance The instance of an object to add.
     * @param type The type of this object.
     * @param <T> The type to bind this implementation to.
     */
    public <T> void add(T instance, Class<T> type) {
        add(new AbstractModule() {

            @Override
            protected void configure() {
                bind(type).toInstance(instance);
            }
        });
    }

    public void add(Module... modules) {
        add(List.of(modules));
    }

    public void add(Collection<Module> modules) {
        logger.debug("Adding {} bindings to dependency injector service during runtime.", modules.size());
        injector = injector.createChildInjector(modules);
    }

    public <T> T getInstance(Class<T> type) {
        logger.debug("Injecting instance of {}.", type);
        return injector.getInstance(type);
    }
}
