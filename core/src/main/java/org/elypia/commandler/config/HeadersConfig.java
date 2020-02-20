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
import org.elypia.commandler.api.HeaderBinder;
import org.elypia.commandler.managers.HeaderManager;
import org.elypia.commandler.utils.ReflectionUtils;
import org.slf4j.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;

/**
 * Configuration for {@link Commandler} event header bindings for
 * the {@link HeaderManager} to use.
 *
 * @author seth@elypia.org (Seth Falco)
 */
@ApplicationScoped
public class HeadersConfig {

    /** Logging with SLF4J. */
    private static final Logger logger = LoggerFactory.getLogger(HeadersConfig.class);

    /**
     * A collection of all binders by type, this does not actually instantiate
     * any of them.
     */
    private final Collection<Class<HeaderBinder>> binderTypes;

    @Inject
    public HeadersConfig(final ConfigService configService) {
        List<String> names = configService.getList(String.class, "commandler.headers.binder");

        if (names != null) {
            binderTypes = ReflectionUtils.convertTypes(names, HeaderBinder.class);
        } else {
            logger.info("Commandler did not find any header bindings defined in the configuration.");
            binderTypes = List.of();
        }
    }

    public Collection<Class<HeaderBinder>> getHeaderBindersType() {
        return Collections.unmodifiableCollection(binderTypes);
    }
}
