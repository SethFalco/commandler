package com.elypia.commandler.metadata;

import com.elypia.commandler.exceptions.AdapterRequiredException;
import com.elypia.commandler.interfaces.Provider;
import com.elypia.commandler.interfaces.*;
import com.elypia.commandler.metadata.data.MetaProvider;
import com.google.inject.*;
import org.slf4j.*;

import java.util.*;

/**
 * The adapters is used to load messages from returning commands
 * or the {@link CommandlerEvent#send(Object)} method.
 *
 * @param <M> The data-type we're building messages into, this
 *            should be the data-type the client of our integrating
 *            platform expects us to send back to users.
 */
public class MessageProvider<M> {

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
            logger.debug("Adapter added from class {}.", provider.getProviderClass().getSimpleName());
    }

    /**
     * Build an object into the {@link M message} using
     * the respective {@link Provider}. <br>
     * When integrating with a platform, you may wish to extend
     * and {@link Override} the load
     * method to accomodate platform specific requirements.
     *
     * @param event The source event that triggered this.
     * @param object The user input after already being parsed by the {@link ParamAdapter}.
     * @return A built message ready to send to the client.
     */
    public M provide(CommandlerEvent<?, M> event, Object object) {
        Objects.requireNonNull(object);
        Provider provider = getProvider(object.getClass());

        M content = (M) provider.provide(object);

        if (content == null)
            throw new IllegalStateException(String.format("String adapter `%s`returned null.", provider.getClass().getName()));

        return content;
    }

    public Provider getProvider(Class<?> typeRequired) {
        return getProvider(typeRequired, Map.of());
    }

    /**
     * Go through the builders and find the most appropriate adapters
     * if one is available for building an message from this
     * data-type.
     *
     * @param typeRequired The data-type we need to load from.
     * @return The adapters to convert this to a message.
     * @throws IllegalArgumentException If no {@link Provider} is
     * registered for this data-type.
     */
    public <T> Provider<?, M> getProvider(Class<?> typeRequired, Map<Class<T>, T> injectables) {
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
            throw new AdapterRequiredException("Provider required for type `%s`.", typeRequired);

        Injector child = injector.createChildInjector(new AbstractModule() {

            @Override
            protected void configure() {
                injectables.forEach((key, value) -> bind(key).toInstance(value));
            }
        });

        return (Provider<?, M>)child.getInstance(provider.getProviderClass());
    }
}
