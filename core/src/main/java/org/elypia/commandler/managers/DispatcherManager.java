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
import org.elypia.commandler.Request;
import org.elypia.commandler.api.Dispatcher;
import org.elypia.commandler.config.DispatcherConfig;
import org.elypia.commandler.event.ActionEvent;
import org.slf4j.*;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import java.util.*;

/**
 * An ordered list of dispatchers to dispatch events
 * that are received appropriately.
 *
 * @author seth@elypia.org (Seth Falco)
 */
@ApplicationScoped
public class DispatcherManager {

    private static final Logger logger = LoggerFactory.getLogger(DispatcherManager.class);

    private final BeanManager beanManager;

    private List<Dispatcher> dispatchers;

    @Inject
    public DispatcherManager(final BeanManager beanManager, final DispatcherConfig config) {
        this.beanManager = beanManager;
        dispatchers = new ArrayList<>();

        for (Class<Dispatcher> dispatcher : config.getDispatcherTypes()) {
            logger.debug("Creating instance of {}.", dispatcher);
            dispatchers.add(BeanProvider.getContextualReference(dispatcher));
        }
    }

    public <S, M> ActionEvent<S, M> dispatch(Request<S, M> request) {
        for (Dispatcher dispatcher : dispatchers) {
            if (!dispatcher.isValid(request))
                continue;

            ActionEvent<S, M> event = dispatcher.parse(request);

            if (event == null)
                continue;

            logger.debug("Used dispatcher for event: {}", dispatcher.getClass());

            return event;
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
