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
import org.elypia.commandler.event.ActionEvent;
import org.elypia.commandler.exceptions.AdapterRequiredException;
import org.elypia.commandler.injection.InjectionService;
import org.elypia.commandler.metadata.MetaMessenger;
import org.slf4j.*;

import javax.inject.*;
import java.util.*;

/**
 * @author seth@elypia.org (Syed Shah)
 */
@Singleton
public class MessengerManager {

    /** SLF4J Logger*/
    private static final Logger logger = LoggerFactory.getLogger(MessengerManager.class);

    /** Used to manage dependency injection when constructions param adapters. */
    private final InjectionService injectionService;

    private final Collection<MetaMessenger> providers;

    @Inject
    public MessengerManager(InjectionService injectionService) {
        this(injectionService, injectionService.getInstances(MetaMessenger.class));
    }

    public MessengerManager(InjectionService injectionService, MetaMessenger... messengers) {
        this(injectionService, List.of(messengers));
    }

    public MessengerManager(InjectionService injectionService, Collection<MetaMessenger> messengers) {
        this.injectionService = Objects.requireNonNull(injectionService);
        this.providers = Objects.requireNonNull(messengers);

        for (MetaMessenger provider : messengers)
            logger.debug("Provider added for type {}.", provider.getProviderType().getSimpleName());
    }

    public <M> M provide(ActionEvent<?, M> event, Object object) {
        return provide(event, object, event.getIntegration());
    }
    public <M> M provide(ActionEvent<?, M> event, Object object, Integration<?, M> controller) {
        return provide(event, object, controller, controller.getMessageType());
    }

    /**
     * Build an message object to send back to the client using
     * the respective {@link ResponseBuilder}.
     *
     * @param controller The controller that received this event.
     * @param object The user input after already being parsed by the {@link AdapterManager}.
     * @return A built message ready to send to the client.
     */
    public <T> T provide(ActionEvent<?, ?> event, Object object, Integration controller, Class<T> type) {
        Objects.requireNonNull(controller);
        Objects.requireNonNull(object);
        ResponseBuilder responseBuilder = getProvider(controller, object.getClass());

        Object content = responseBuilder.provide(event, object);

        if (content == null)
            throw new IllegalStateException(String.format("String adapter `%s`returned null.", responseBuilder.getClass().getName()));

        if (type.isAssignableFrom(content.getClass()))
            return (T)content;

        throw new RuntimeException("Provider did not produce the type required.");
    }

    /**
     * Go through the builders and find the most appropriate adapters
     * if one is available for building an message from this
     * data-type.
     *
     * @param typeRequired The data-type we need to load from.
     * @return The adapters to convert this to a message.
     * @throws IllegalArgumentException If no {@link ResponseBuilder} is
     * registered for this data-type.
     */
    public <T> ResponseBuilder<?, ?> getProvider(Integration controller, Class<?> typeRequired) {
        MetaMessenger provider = null;

        for (MetaMessenger metaMessenger : providers) {
            Collection<Class<?>> compatible = metaMessenger.getCompatibleTypes();

            if (metaMessenger.getBuildType() != controller.getMessageType())
                continue;

            if (compatible.contains(typeRequired)) {
                provider = metaMessenger;
                break;
            }

            if (compatible.stream().anyMatch(c -> c.isAssignableFrom(typeRequired)))
                provider = metaMessenger;
        }

        if (provider == null)
            throw new AdapterRequiredException("ResponseBuilder required for type " + typeRequired + ".");

        return injectionService.getInstance(provider.getProviderType());
    }
}
