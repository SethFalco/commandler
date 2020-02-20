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

package org.elypia.commandler.config;

import org.elypia.commandler.Commandler;
import org.elypia.commandler.api.Dispatcher;
import org.elypia.commandler.utils.ReflectionUtils;
import org.slf4j.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;

/**
 * Configuration for which {@link Dispatcher}s are setup for this
 * {@link Commandler} application.
 *
 * All {@link Commandler} applications must have at least one
 * {@link Dispatcher} implementations available at runtime to
 * process any commands.
 *
 * @author seth@elypia.org (Seth Falco)
 */
@ApplicationScoped
public class DispatcherConfig {

    /** Logging with SLF4J. */
    private static final Logger logger = LoggerFactory.getLogger(DispatcherConfig.class);

    /** A collection of dispatchers that will be created when this Commandler context is run. */
    private Collection<Class<Dispatcher>> dispatcherTypes;

    @Inject
    public DispatcherConfig(final ConfigService configService) {
        List<String> names = configService.getList(String.class, "commandler.dispatcher.class");

        if (names != null) {
            dispatcherTypes = ReflectionUtils.convertTypes(names, Dispatcher.class);
        } else {
            logger.warn("Commandler did not find any dispatchers defined in the configuration.");
            dispatcherTypes = List.of();
        }
    }

    public Collection<Class<Dispatcher>> getDispatcherTypes() {
        return Collections.unmodifiableCollection(dispatcherTypes);
    }
}
