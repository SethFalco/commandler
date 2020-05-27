/*
 * Copyright 2019-2020 Elypia CIC
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

import org.apache.deltaspike.cdise.api.*;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.elypia.commandler.api.Integration;
import org.elypia.commandler.event.ActionEvent;
import org.slf4j.*;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.CDI;
import javax.enterprise.util.TypeLiteral;
import java.lang.annotation.Annotation;

/**
 * The root {@link Commandler} class, this ultimately enables your
 * {@link Commandler} Application with your configuration and runtime dependencies.
 *
 * There are two main means of configurable {@link Commandler}.
 *
 * <strong>Static Configuration:</strong> This entails configuration files either in the
 * classpath or externally, and {@link Annotation}s.
 * <strong>Dependency Injection Modules:</strong> This entails overriding runtime dependencies
 * for the CDI/IoC container to use.
 *
 * @author seth@elypia.org (Seth Falco)
 */
@ApplicationScoped
public class Commandler {

    /** Logging with slf4j. */
    private static final Logger logger = LoggerFactory.getLogger(Commandler.class);

    /**
     * Construct an instance of Commandler, in most cases this will be used
     * to manage your application, but in some cases may just be a small instance amongst
     * multiple instance or for a small part of your application.
     *
     * This will kick off the CDI container and start initializating the bulk of the
     * application. This will not however create {@link Integration}s so commands
     * will not be accepted yet by the application.
     *
     * Call {@link #run()} to initialize all {@link Integration}s.
     *
     * @return The global Commandler instance.
     */
    public static Commandler create() {
        logger.debug("Started Commandler, loading all configuration and dependencies.");

        CdiContainer container = CdiContainerLoader.getCdiContainer();
        container.boot();
        container.getContextControl().startContexts();
        logger.debug("Initialized {} for dependency injection.", CdiContainer.class);

        return BeanProvider.getContextualReference(Commandler.class);
    }

    /**
     * Instantiate all {@link Integration}s and start receiving and
     * handling {@link ActionEvent}s receive .
     *
     * This actually runs the Commandler instance and should be used to run any chat
     * bots by whatever interactive means.
     */
    public void run() {
        TypeLiteral<? extends Integration<?, ?>> typeLiteral = new TypeLiteral<>(){};
        Instance<? extends Integration<?, ?>> integrations = CDI.current().select(typeLiteral);

        long total = integrations.stream().count();

        if (total == 0)
            logger.warn("No integrations were defined, events will not be caught.");

        else if (total > 1)
            logger.warn("Detected multiple integrations in the same runtime; it's recommended to submodule the seperate integrations rather than run a monilith.");

        for (Integration<?, ?> integration : integrations) {
            integration.init();
            logger.info("Created instance of {} which uses {} for messages.", integration, integration.getMessageType());
        }
    }
}
