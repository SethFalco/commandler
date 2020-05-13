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
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.text.NumberFormat;

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
 * @author seth@elypia.org (Seth Falco)
 */
@ApplicationScoped
public class Commandler {

    /** Logging with slf4j. */
    private static final Logger logger = LoggerFactory.getLogger(Commandler.class);

    /** Metadata and configurationa associated with this Commandler instance. */
    private final AppContext appContext;

    /**
     * Construct an instance of Commandler, in most cases this will be used
     * to manage your application, but in some cases may just be a small instance amongst
     * multiple instance or for a small part of your application.
     *
     * This will only load and initialize as much of Commandler as possible without
     * actually connecting to any APIs or processing commands, this will ensure Commandler
     * can load and validate everything before any attempt to go live to users.
     *
     * @param
     */
    @Inject
    public Commandler(final AppContext context, Integration<?, ?> type) {
        this.appContext = context;
        logger.info("Loaded all configuration and dependencies in {}.", appContext.getTimeSinceStartupFormatted());
        type.toString();
    }

    public static Commandler create() {
        logger.debug("Started Commandler, loading all configuration and dependencies.");

        CdiContainer container = CdiContainerLoader.getCdiContainer();
        container.boot();
        container.getContextControl().startContexts();
        logger.debug("Initialized {} for dependency injection.", CdiContainer.class);

        return BeanProvider.getContextualReference(Commandler.class);
    }

    /**
     * TODO: Allow multiple integrations to run at a time?
     *
     * Instantiate all {@link Integration}s and start receiving and
     * handling {@link ActionEvent}s receive .
     *
     * This actually runs the Commandler instance and should be used to run any chat
     * bots by whatever interactive means.
     */
    public void run() {
//        Class<? extends Integration<?, ?>> type;
//
//        Integration<?, ?> integration = BeanProvider.getContextualReference(type.getClass());
//
//        if (integration.getMessageType() == null)
//            logger.warn("Integration has been instantiated but no message types have been defined, unable to perform commands.");
    }

    @Produces
    public NumberFormat getNumberFormat() {
        return NumberFormat.getInstance();
    }

    public AppContext getAppContext() {
        return appContext;
    }
}
