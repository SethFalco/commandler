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

import org.elypia.commandler.api.Integration;
import org.elypia.commandler.event.ActionEvent;

import javax.inject.Singleton;

/**
 * The root {@link Commandler} class, this ultimately enables your
 * {@link Commandler} Application with your configuration and runtime dependencies.
 *
 * There are two main means of configurable {@link Commandler}.
 *
 * <strong>Static Configuration:</strong> This entails configuration files either in the
 * classpath or externally, and {@link java.lang.annotation.Annotation}s.
 * <strong>Dependency Injection Modules:</strong> This entails overriding runtime dependencies
 * for the IoC container to use.
 *
 * @author seth@elypia.org (Syed Shah)
 */
@Singleton
public class Commandler {

    /** Metadata and configurationa associated with this Commandler instance. */
    private final AppContext appContext;

    public Commandler() {
        appContext = new AppContext();
    }

    /**
     * Instantiate all {@link Integration}s and start receiving and
     * handling {@link ActionEvent}s recieved.
     */
    public void run() {

    }

    public AppContext getAppContext() {
        return appContext;
    }
}
