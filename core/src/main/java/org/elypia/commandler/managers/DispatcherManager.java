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

import org.elypia.commandler.api.*;
import org.elypia.commandler.config.CommandlerConfig;
import org.elypia.commandler.event.ActionEvent;
import org.elypia.commandler.injection.InjectorService;
import org.slf4j.*;

import javax.inject.*;
import java.util.*;

/**
 * An ordered list of dispatchers to dispatch events
 * that are received appropriately.
 *
 * @author seth@elypia.org (Syed Shah)
 */
@Singleton
public class DispatcherManager {

    private static final Logger logger = LoggerFactory.getLogger(DispatcherManager.class);

    private List<Dispatcher> dispatchers;

    @Inject
    public DispatcherManager(InjectorService injector, final CommandlerConfig config) {
        List<Dispatcher> dispatchers = new ArrayList<>();

        for (Class<Dispatcher> dispatcher : config.getDispatchers()) {
            logger.debug("Creating instance of {}.", dispatcher);
            dispatchers.add(injector.getInstance(dispatcher));
        }

        this.dispatchers = new ArrayList<>();
        this.dispatchers.addAll(dispatchers);
    }

    public DispatcherManager(Dispatcher... dispatchers) {
        this(List.of(dispatchers));
    }

    public DispatcherManager(Collection<Dispatcher> dispatchers) {
        this.dispatchers = new ArrayList<>();

        if (dispatchers != null)
            this.dispatchers.addAll(dispatchers);
    }

    public <S, M> ActionEvent<S, M> dispatch(Integration<S, M> integration, S event, String content) {
        logger.debug("Dispatched event with content: {}", content);

        for (Dispatcher dispatcher : dispatchers) {
            if (!dispatcher.isValid(event, content))
                continue;

            logger.debug("Using dispatcher for event: {}", dispatcher.getClass());
            return dispatcher.parse(integration, event, content);
        }

        return null;
    }

    public void add(Dispatcher... dispatchers) {
        add(List.of(dispatchers));
    }

    public void add(Collection<Dispatcher> dispatchers) {
        this.dispatchers.addAll(dispatchers);
    }
}
