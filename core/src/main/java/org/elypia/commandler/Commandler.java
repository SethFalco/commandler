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

package org.elypia.commandler;

import com.google.inject.Module;
import com.google.inject.*;
import org.elypia.commandler.api.*;
import org.elypia.commandler.configuration.*;
import org.elypia.commandler.event.ActionEvent;

import javax.inject.Singleton;
import java.util.List;

/**
 * The root {@link Commandler} class, this ultimately enables your
 * {@link Commandler} Application with your configuration and runtime dependencies.
 *
 * There are two main means of configurable {@link Commandler}.
 *
 * <strong>Static Configuration:</strong> This entails configuration files either in the
 * classpath or externally, and {@link java.lang.annotation.Annotation}s.
 * <strong>Dependency Injection Modules:</strong> This entails overriding runtime dependencies
 * for {@link Guice} to inject into dependencies and {@link Controller}s.
 *
 * @author seth@elypia.org (Syed Shah)
 */
@Singleton
public class Commandler {

    /** The configuration for this instance of {@link Commandler}. */
    private CommandlerConfiguration config;

    /** The {@link Guice} {@link Injector} to inject dependencies for Commandler. */
    private Injector injector;

    /** A list of action listeners to send action events to. */
    private List<ActionListener> listeners;

    /**
     * Instantiate {@link Commandler} using the specified dependency injection
     * modules, or with no arguments if no additional modules should be specified.
     *
     * @param modules Additional dependency injection modules to add.
     */
    public Commandler(Module... modules) {
        this.injector = Guice.createInjector(Stage.PRODUCTION, modules);
        this.injector = injector.createChildInjector(new CommandlerModule(this));
        this.config = injector.getInstance(CommandlerConfiguration.class);
    }

    /**
     * Instantiate all {@link Integration}s and start receiving and
     * handling {@link ActionEvent}s recieved.
     */
    public void run() {
        var test = injector.getInstance(ContextConverter.class).convertIntegrations();

        for (Class<Integration<?, ?>> integrationType : test)
            injector.getInstance(integrationType);

        listeners.add(injector.getInstance(ActionHandler.class));
    }

    public <S, M> M onAction(Integration<S, M> integration, S source, String content) {
        for (ActionListener listener : listeners) {
            M object = listener.onAction(integration, source, content);

            if (object != null)
                return object;
        }

        return null;
    }

    public void addListeners(ActionListener... listeners) {
        this.listeners.addAll(List.of(listeners));
    }

    public CommandlerConfiguration getConfig() {
        return config;
    }
}
