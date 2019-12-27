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

import org.elypia.commandler.*;
import org.elypia.commandler.api.*;
import org.elypia.commandler.config.CommandlerConfig;
import org.elypia.commandler.event.ActionEvent;
import org.elypia.commandler.exceptions.AdapterRequiredException;
import org.elypia.commandler.metadata.MetaMessenger;
import org.slf4j.*;

import javax.inject.*;
import java.util.*;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@Singleton
public class MessengerManager {

    /** SLF4J Logger*/
    private static final Logger logger = LoggerFactory.getLogger(MessengerManager.class);

    /** Used to manage dependency injection when injecting messengers. */
    private final CdiInjector injector;
    private final CommandlerConfig commandlerConfig;

    @Inject
    public MessengerManager(final CdiInjector injector, final CommandlerConfig commandlerConfig) {
        this.injector = Objects.requireNonNull(injector);
        this.commandlerConfig = Objects.requireNonNull(commandlerConfig);
    }

    /**
     * Build an message object to send back to the client using
     * the respective {@link Messenger}.
     *
     * @param event The action event the was processed by {@link Commandler}.
     * @param object The object that should be sent in chat.
     * @param integration The integration that receives this event.
     * @return A built message ready to send to the client.
     */
    public <M> M provide(ActionEvent<?, M> event, Object object, Integration<?, M> integration) {
        Objects.requireNonNull(integration);
        Objects.requireNonNull(object);
        Messenger messenger = getProvider(integration, object.getClass());

        Object content = messenger.provide(event, object);

        if (content == null)
            throw new IllegalStateException(String.format("String adapter `%s`returned null.", messenger.getClass().getName()));

        if (integration.getMessageType().isAssignableFrom(content.getClass()))
            return integration.getMessageType().cast(content);

        throw new RuntimeException("Provider did not produce the type required.");
    }

    /**
     * Go through the builders and find the most appropriate adapters
     * if one is available for building an message from this
     * data-type.
     *
     * @param typeRequired The data-type we need to load from.
     * @param <S> The source event the {@link Integration} provides.
     * @param <M> The type of message the {@link Integration} sends and receives.
     * @param <O> The object which a provider is required for.
     * @return The messenger to convert this to a message.
     * @throws IllegalArgumentException If no {@link Messenger} is
     * registered for this data-type.
     */
    public <S, M, O> Messenger<O, M> getProvider(Integration<S, M> controller, Class<O> typeRequired) {
        MetaMessenger provider = null;

        for (MetaMessenger metaMessenger : commandlerConfig.getMessengers()) {
            Collection<Class<Object>> compatible = metaMessenger.getCompatibleTypes();

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

        return injector.getInstance(provider.getProviderType());
    }
}
