package com.elypia.commandler.core;

import com.elypia.commandler.CommandlerEvent;
import com.elypia.commandler.exceptions.init.AdapterRequiredException;
import com.elypia.commandler.interfaces.ResponseProvider;
import com.elypia.commandler.meta.data.MetaProvider;
import com.google.inject.*;
import org.slf4j.*;

import java.util.*;

public class MessageProvider {

    /**
     * We're using SLF4J to manage logging, remember to use a binding / implementation
     * and configure logging for when testing or running an application.
     */
    private static final Logger logger = LoggerFactory.getLogger(MessageProvider.class);

    /** Used to manage dependency injection when constructions param adapters. */
    private final Injector injector;
    private final Collection<MetaProvider> providers;

    public MessageProvider(MetaProvider... adapters) {
        this(Guice.createInjector(), adapters);
    }

    public MessageProvider(Injector injector, MetaProvider... adapters) {
        this(injector, List.of(adapters));
    }

    public MessageProvider(Injector injector, Collection<MetaProvider> providers) {
        this.injector = Objects.requireNonNull(injector);
        this.providers = Objects.requireNonNull(providers);

        for (MetaProvider provider : providers)
            logger.debug("Provider added for type {}.", provider.getProviderType().getSimpleName());
    }

    /**
     * Build an object into the {@link M message} using
     * the respective {@link ResponseProvider}. <br>
     * When integrating with a platform, you may wish to extend
     * and {@link Override} the load
     * method to accomodate platform specific requirements.
     *
     * @param event The source event that triggered this.
     * @param object The user input after already being parsed by the {@link ParamAdapter}.
     * @return A built message ready to send to the client.
     */
    public M provide(CommandlerEvent<?> event, Object object) {
        Objects.requireNonNull(object);
        ResponseProvider responseProvider = getProvider(object.getClass());

        M content = (M) responseProvider.provide(object);

        if (content == null)
            throw new IllegalStateException(String.format("String adapter `%s`returned null.", responseProvider.getClass().getName()));

        return content;
    }

    public ResponseProvider getProvider(Class<?> typeRequired) {
        return getProvider(typeRequired, Map.of());
    }

    /**
     * Go through the builders and find the most appropriate adapters
     * if one is available for building an message from this
     * data-type.
     *
     * @param typeRequired The data-type we need to load from.
     * @return The adapters to convert this to a message.
     * @throws IllegalArgumentException If no {@link ResponseProvider} is
     * registered for this data-type.
     */
    public <T> ResponseProvider<?, M> getProvider(Class<?> typeRequired, Map<Class<T>, T> injectables) {
        MetaProvider provider = null;

        for (MetaProvider metaProvider : providers) {
            Collection<Class<?>> compatible = metaProvider.getCompatibleClasses();

            if (compatible.contains(typeRequired)) {
                provider = metaProvider;
                break;
            }

            if (compatible.stream().anyMatch(c -> c.isAssignableFrom(typeRequired)))
                provider = metaProvider;
        }

        if (provider == null)
            throw new AdapterRequiredException("ResponseProvider required for type `%s`.", typeRequired);

        Injector child = injector.createChildInjector(new AbstractModule() {

            @Override
            protected void configure() {
            injectables.forEach((key, value) -> bind(key).toInstance(value));
            }
        });

        return (ResponseProvider<?, M>)child.getInstance(provider.getProviderClass());
    }
}
