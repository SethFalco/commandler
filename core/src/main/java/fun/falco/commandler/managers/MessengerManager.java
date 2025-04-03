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

import java.util.Collection;
import java.util.Objects;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.deltaspike.core.api.provider.BeanProvider;

import fun.falco.commandler.Commandler;
import fun.falco.commandler.CommandlerExtension;
import fun.falco.commandler.api.Integration;
import fun.falco.commandler.api.Messenger;
import fun.falco.commandler.event.ActionEvent;
import fun.falco.commandler.exceptions.AdapterRequiredException;
import fun.falco.commandler.metadata.MetaMessenger;

/**
 * @author seth@falco.fun (Seth Falco)
 */
@ApplicationScoped
public class MessengerManager {

    private final CommandlerExtension commandlerExtension;

    @Inject
    public MessengerManager(final CommandlerExtension commandlerExtension) {
        this.commandlerExtension = Objects.requireNonNull(commandlerExtension);
    }

    /**
     * Build an message object to send back to the client using the respective
     * {@link Messenger}.
     *
     * @param event Action event the was processed by {@link Commandler}.
     * @param object Object that should be sent in chat.
     * @return Built message ready to send to the client.
     */
    public <M> M provide(ActionEvent<?, M> event, Object object) {
        Objects.requireNonNull(object);
        Integration<?, M> integration = event.getRequest().getIntegration();
        Messenger messenger = getMessenger(integration, object.getClass());

        Object content = messenger.provide(event, object);

        if (content == null) {
            throw new IllegalStateException(String.format("String adapter `%s`returned null.", messenger.getClass().getName()));
        }

        Class<M> messageType = integration.getMessageType();

        if (messageType.isAssignableFrom(content.getClass())) {
            return messageType.cast(content);
        }

        throw new IllegalStateException("Provider did not produce the type required.");
    }

    /**
     * Go through the builders and find the most appropriate adapters if one is
     * available for building an message from this data-type.
     *
     * @param integration Platform that this event was received from.
     * @param typeRequired Data-type we need to load from.
     * @param <S> Source event the {@link Integration} provides.
     * @param <M> Type of message the {@link Integration} sends and receives.
     * @param <O> Object which a provider is required for.
     * @return Messenger to convert this to a message.
     * @throws IllegalArgumentException
     *      If no {@link Messenger} is registered for this data-type.
     */
    public <S, M, O> Messenger<O, M> getMessenger(Integration<S, M> integration, Class<O> typeRequired) {
        MetaMessenger provider = null;

        for (MetaMessenger metaMessenger : commandlerExtension.getMetaMessengers()) {
            Collection<Class<?>> compatible = metaMessenger.getCompatibleTypes();

            if (metaMessenger.getBuildType() != integration.getMessageType()) {
                continue;
            }

            if (compatible.contains(typeRequired)) {
                provider = metaMessenger;
                break;
            }

            if (compatible.stream().anyMatch(c -> c.isAssignableFrom(typeRequired))) {
                provider = metaMessenger;
            }
        }

        if (provider == null) {
            throw new AdapterRequiredException(Messenger.class + " required for type " + typeRequired + ".");
        }

        return BeanProvider.getContextualReference(provider.getProviderType());
    }
}
