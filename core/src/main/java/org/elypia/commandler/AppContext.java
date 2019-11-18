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

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.elypia.commandler.api.Integration;
import org.elypia.commandler.config.ConfigService;
import org.elypia.commandler.injection.*;
import org.slf4j.*;

import javax.inject.Singleton;

/**
 * The Application Context is the heart of any Commandler application or instance.
 * This contains all objects that ultimately allow the application to run, find
 * meta data, contruct controllers or other core objects on-demand and receive
 * messages from {@link Integration}s.
 *
 * @author seth@elypia.org (Seth Falco)
 */
@Singleton
public class AppContext {

    /** Logging using the SLF4J API. */
    private static final Logger logger = LoggerFactory.getLogger(AppContext.class);

    /** The configuration for this instance of {@link Commandler}. */
    private final ConfigService config;

    /** The injectotion framework abstraction to inject dependencies. */
    private final InjectorService injector;

    /** The unix epoch that this context was created. */
    private final long STARTUP_TIME;

    public AppContext(Commandler commandler) {
        this.STARTUP_TIME = System.currentTimeMillis();
        logger.debug("Generating Commandler AppContext at epoch: {}", STARTUP_TIME);

        injector = new InjectorService();

        try {
            config = new ConfigService();
            logger.debug("Finished loading all configuration.");
        } catch (ConfigurationException e) {
            throw new RuntimeException(e);
        }

        injector.add(new CommandlerModule(commandler, this));
        logger.debug("Added default injection bindings to {}.", InjectorService.class.getSimpleName());
    }

    public ConfigService getConfig() {
        return config;
    }

    public InjectorService getInjector() {
        return injector;
    }

    public long getStartupTime() {
        return STARTUP_TIME;
    }

    public long getTimeSinceStartup() {
        return System.currentTimeMillis() - getStartupTime();
    }

    public String getTimeSinceStartupFormatted() {
        return String.format("%,dms", getTimeSinceStartup());
    }
}
