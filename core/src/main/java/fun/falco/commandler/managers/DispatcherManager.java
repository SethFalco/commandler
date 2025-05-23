/*
 * Copyright 2019-2025 Seth Falco and Commandler Contributors
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

package fun.falco.commandler.managers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fun.falco.commandler.CommandlerExtension;
import fun.falco.commandler.api.Dispatcher;
import fun.falco.commandler.event.ActionEvent;
import fun.falco.commandler.event.Request;

/**
 * Ordered list of dispatchers to dispatch events that are received
 * appropriately.
 *
 * @author seth@falco.fun (Seth Falco)
 */
@ApplicationScoped
public class DispatcherManager {

    private static final Logger logger = LoggerFactory.getLogger(DispatcherManager.class);

    private List<Dispatcher> dispatchers;

    @Inject
    public DispatcherManager(final CommandlerExtension extension) {
        dispatchers = new ArrayList<>();

        for (Class<? extends Dispatcher> dispatcher : extension.getDispatcherTypes()) {
            logger.debug("Creating instance of {}.", dispatcher);
            dispatchers.add(BeanProvider.getContextualReference(dispatcher));
        }
    }

    public <S, M> ActionEvent<S, M> dispatch(Request<S, M> request) {
        for (Dispatcher dispatcher : dispatchers) {
            if (!dispatcher.isValid(request)) {
                continue;
            }

            ActionEvent<S, M> event = dispatcher.parse(request);

            if (event == null) {
                continue;
            }

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
