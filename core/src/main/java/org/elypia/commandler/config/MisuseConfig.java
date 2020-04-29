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

import org.elypia.commandler.api.MisuseHandler;
import org.elypia.commandler.utils.ReflectionUtils;
import org.slf4j.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;

/**
 * Configuration for which {@link MisuseHandler} implementations
 * to use at runtime.
 *
 * @author seth@elypia.org (Seth Falco)
 */
@ApplicationScoped
public class MisuseConfig {

    /** Logging with SLF4J. */
    private static final Logger logger = LoggerFactory.getLogger(MisuseConfig.class);

    /** A collection of misuse handlers that will be created when this Commandler context is run.  */
    private Collection<Class<MisuseHandler>> misuseHandlerTypes;

    @Inject
    public MisuseConfig(final ConfigService configService) {
        List<String> names = configService.getList(String.class, "commandler.misuse");

        if (names != null)
            misuseHandlerTypes = ReflectionUtils.convertTypes(names, MisuseHandler.class);
        else {
            logger.debug("Commandler did not find any misuse handlers defined in the configuration.");
            misuseHandlerTypes =  List.of();
        }
    }

    public Collection<Class<? extends MisuseHandler>> getMisuseHandlerTypes() {
        return Collections.unmodifiableCollection(misuseHandlerTypes);
    }
}
