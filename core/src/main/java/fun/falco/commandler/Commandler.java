/*
 * Copyright 2019-2025 Seth Falco and Commandler Contributors
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

package fun.falco.commandler;

import java.lang.annotation.Annotation;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.CDI;
import javax.enterprise.util.TypeLiteral;

import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.apache.deltaspike.core.util.ProjectStageProducer;
import fun.falco.commandler.api.Integration;
import fun.falco.commandler.event.ActionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * @author seth@falco.fun (Seth Falco)
 */
@ApplicationScoped
public class Commandler {

    /** Logging with SLF4J. */
    private static final Logger logger = LoggerFactory.getLogger(Commandler.class);

    private static CdiContainer cdiContainer;

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
        logger.info("DeltaSpike computed the following ProjectStage: {}", ProjectStageProducer.getInstance().getProjectStage());
        logger.debug("Started Commandler, loading all configuration and dependencies.");

        cdiContainer = CdiContainerLoader.getCdiContainer();
        cdiContainer.boot();
        cdiContainer.getContextControl().startContexts();
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

        if (total == 0) {
            logger.warn("No integrations were defined, events will not be caught.");
        } else if (total > 1) {
            logger.warn("Detected multiple integrations in the same runtime; it's recommended to submodule the separate integrations rather than run a monolith.");
        }

        for (Integration<?, ?> integration : integrations) {
            integration.init();
            logger.info("Created instance of {} which uses {} for messages.", integration, integration.getMessageType());
        }
    }

    /**
     * Shutdown all CDI contexts and shutdown the CDI container
     * managed by Commandler.
     *
     * This should be used to gracefully stop any command handling.
     */
    public static void stop() {
        logger.info("Stopping Commandler managed CDI container.");
        cdiContainer.getContextControl().stopContexts();
        cdiContainer.shutdown();
        logger.info("Stopped Commander managed CDI container.");
    }
}
