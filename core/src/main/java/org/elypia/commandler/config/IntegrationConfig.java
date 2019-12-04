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

package org.elypia.commandler.config;

import org.elypia.commandler.Commandler;
import org.elypia.commandler.api.Integration;
import org.elypia.commandler.utils.ReflectionUtils;
import org.slf4j.*;

import javax.inject.*;
import java.util.*;

/**
 * Configuration to manage all {@link Integration} available
 * in this {@link Commandler} application.
 *
 * @author seth@elypia.org (Seth Falco)
 */
@Singleton
public class IntegrationConfig {

    /** Logging with SLF4J. */
    private static final Logger logger = LoggerFactory.getLogger(IntegrationConfig.class);

    /**
     * A collection of integrations that will be creates when this Commandler context is run.
     * These {@link Integration} are <strong>NOT</strong> instantiated before {@link Commandler#run()}
     * is called.
     */
    private Collection<Class<Integration>> integrationTypes;

    @Inject
    public IntegrationConfig(final ConfigService configService) {
        List<String> names = configService.getList(String.class, "commandler.integration");

        if (names != null) {
            integrationTypes = ReflectionUtils.convertTypes(names, Integration.class);
        } else {
            logger.warn("Commandler did not find any integrations defined in the configuration.");
            integrationTypes = List.of();
        }
    }

    public Collection<Class<Integration>> getIntegrationTypes() {
        return Collections.unmodifiableCollection(integrationTypes);
    }
}
