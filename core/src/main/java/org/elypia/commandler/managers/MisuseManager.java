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

package org.elypia.commandler.managers;

import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.elypia.commandler.api.MisuseHandler;
import org.elypia.commandler.config.MisuseConfig;
import org.elypia.commandler.exceptions.misuse.MisuseException;
import org.elypia.commandler.metadata.MetaCommand;
import org.slf4j.*;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import java.util.Objects;

/**
 * Whenever exceptions occur, this could be logical exceptions in the application,
 * internal Commandler exceptions due to a bug, or exception thrown by a
 * {@link MetaCommand} or Manager class due to some kind of user misuse.
 *
 * This class will handle the exceptions produced in runtime and report the
 * appropriate message to users. If a type of {@link Exception} is not
 * handled, then no message it sent to users.
 *
 * @author seth@elypia.org (Seth Falco)
 */
@ApplicationScoped
public class MisuseManager {

    /**
     * We're using SLF4J to manage logging, remember to use a binding / implementation
     * and configure logging for when testing or running an application.
     */
    private static final Logger logger = LoggerFactory.getLogger(MisuseManager.class);

    private final BeanManager beanManager;

    /** The configuration class which contains all metadata for this instance. */
    private final MisuseConfig misuseConfig;

    @Inject
    public MisuseManager(final BeanManager beanManager, final MisuseConfig misuseConfig) {
        this.beanManager = beanManager;
        this.misuseConfig = Objects.requireNonNull(misuseConfig);
    }

    /**
     * @param ex The exception that needs to be handled.
     * @param <X> The type of exception that occured.
     * @return The message to send due to this exception,
     * or null if there is no message to be sent.
     */
    public <X extends MisuseException> Object handle(X ex) {
        for (Class<? extends MisuseHandler> type : misuseConfig.getMisuseHandlerTypes()) {
            Object o = BeanProvider.getContextualReference(type).handle(ex);

            if (o != null)
                return o;
        }

        return null;
    }
}
