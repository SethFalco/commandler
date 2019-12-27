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

import org.elypia.commandler.config.ConfigService;
import org.slf4j.*;

import javax.enterprise.inject.se.*;
import java.io.Closeable;

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
public class CdiInjector implements Closeable {

    private static final Logger logger = LoggerFactory.getLogger(CdiInjector.class);

    private SeContainer container;

    public CdiInjector(ConfigService config, Class<?>... packageClasses) {
        container = SeContainerInitializer.newInstance()
            .disableDiscovery()
            .setProperties(config.getPropertyMap())
            .addPackages(true, Commandler.class)
            .addPackages(true, packageClasses)
            .initialize();

        logger.debug("Instantiated {} for dependency injection.", CdiInjector.class);
    }

    public <T> T getInstance(Class<T> type) {
        logger.debug("Injecting instance of {}.", type);
        return container.select(type).get();
    }

    public SeContainer getSeContainer() {
        return container;
    }

    @Override
    public void close() {
        container.close();
    }
}
