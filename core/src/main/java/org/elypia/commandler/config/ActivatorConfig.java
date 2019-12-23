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

import org.apache.commons.configuration2.ImmutableHierarchicalConfiguration;
import org.slf4j.*;

import javax.inject.*;
import java.util.*;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@Singleton
public class ActivatorConfig {

    /** Logging with SLF4J. */
    private static final Logger logger = LoggerFactory.getLogger(ActivatorConfig.class);

    private Map<String, String> propertyNames;

    @Inject
    public ActivatorConfig(final ConfigService configService) {
        propertyNames = new HashMap<>();

        List<ImmutableHierarchicalConfiguration> activatorConfigs = configService.getConfiguration()
            .immutableConfigurationsAt("commandler.activator");

        for (ImmutableHierarchicalConfiguration activatorConfig : activatorConfigs)
            propertyNames.put(activatorConfig.getString("property"), activatorConfig.getString("display-name"));


        if (propertyNames.isEmpty())
            logger.warn("No registered activators, Commandler can still perform commands but won't be able to tell users in static locations.");
    }

    public Map<String, String> getActivators() {
        return Collections.unmodifiableMap(propertyNames);
    }
}
