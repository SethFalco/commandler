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

package org.elypia.commandler.middleware;

import org.elypia.commandler.api.*;
import org.elypia.commandler.event.ActionEvent;
import org.slf4j.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * Adds a cache step to the registered {@link ActionListener}
 * in order to cache actions that occur during runtime.
 *
 * This can be useful for applications that want to record
 * may need to reference future events to older ones.
 *
 *
 * @author seth@elypia.org (Seth Falco)
 */
@ApplicationScoped
public class CacheMiddleware implements HandlerMiddleware {

    private static final Logger logger = LoggerFactory.getLogger(CacheMiddleware.class);

    private final ActionCache cache;

    @Inject
    public CacheMiddleware(ActionCache cache) {
        this.cache = cache;
    }

    @Override
    public <S, M> void onMiddleware(ActionEvent<S, M> event) {
        cache.put(event.getAction());
    }
}
