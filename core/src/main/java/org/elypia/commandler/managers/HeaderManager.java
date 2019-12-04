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

package org.elypia.commandler.managers;

import org.elypia.commandler.Request;
import org.elypia.commandler.api.HeaderBinder;
import org.elypia.commandler.config.HeadersConfig;
import org.elypia.commandler.injection.InjectorService;
import org.slf4j.*;

import javax.inject.*;
import java.util.*;

/**
 * Manage {@link HeaderBinder}s and actually add
 * headers to events before command processing.
 *
 * @author seth@elypia.org (Seth Falco)
 */
@Singleton
public class HeaderManager {

    /** Logging with SLF4J. */
    private static final Logger logger = LoggerFactory.getLogger(HeaderManager.class);

    /**
     * A collection of header binders, used to bindHeaders metadata for
     * {@link org.elypia.commandler.event.ActionEvent} programatically
     * to the events as they occur.
     */
    private final HeadersConfig config;

    /** Used to get the implementations of each {@link HeaderBinder}. */
    private InjectorService injector;

    private Collection<Class<HeaderBinder>> binders;

    @Inject
    public HeaderManager(final HeadersConfig config, final InjectorService injector) {
        this.config = config;

        if (config == null) {
            logger.info("HeaderManager instantiated with NO headers configuration.");
            return;
        }

        this.injector = Objects.requireNonNull(injector);
        this.binders = config.getHeaderBindersType();
    }

    public <S, M> void bindHeaders(Request<S, M> request) {
        for (Class<HeaderBinder> binderType : binders) {
            Map<String, String> h = injector.getInstance(binderType).bind(request);

            for (Map.Entry<String, String> entry : h.entrySet())
                request.setHeader(entry.getKey(), entry.getValue());
        }
    }
}
