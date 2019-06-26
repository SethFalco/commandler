package com.elypia.commandler.managers;

import com.elypia.commandler.CommandlerEvent;
import com.elypia.commandler.exceptions.AdapterRequiredException;
import com.elypia.commandler.interfaces.*;
import com.elypia.commandler.metadata.data.MetaProvider;
import com.google.inject.*;
import org.slf4j.*;

import java.util.*;

public class ResponseManager {

    /** SLF4J Logger*/
    private static final Logger logger = LoggerFactory.getLogger(ResponseManager.class);

    /** Used to manage dependency injection when constructions param adapters. */
    private final Injector injector;

    private final Collection<MetaProvider> providers;

    public ResponseManager(MetaProvider... adapters) {
        this(Guice.createInjector(), adapters);
    }

    public ResponseManager(Injector injector, MetaProvider... adapters) {
        this(injector, List.of(adapters));
    }

    public ResponseManager(Injector injector, Collection<MetaProvider> providers) {
        this.injector = Objects.requireNonNull(injector);
        this.providers = Objects.requireNonNull(providers);

        for (MetaProvider provider : providers)
            logger.debug("Provider added for type {}.", provider.getProviderType().getSimpleName());
    }

    /**
     * Build an message object to send back to the client using
     * the respective {@link ResponseProvider}.
     *
     * @param controller The controller that received this event.
     * @param object The user input after already being parsed by the {@link AdapterManager}.
     * @return A built message ready to send to the client.
     */
    public Object provide(CommandlerEvent<?> event, Controller controller, Object object) {
        Objects.requireNonNull(controller);
        Objects.requireNonNull(object);
        ResponseProvider responseProvider = getProvider(controller, object.getClass(), Map.of());

        Object content = responseProvider.provide(event, object);

        if (content == null)
            throw new IllegalStateException(String.format("String adapter `%s`returned null.", responseProvider.getClass().getName()));

        return content;
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
    public <T> ResponseProvider<?, ?> getProvider(Controller controller, Class<?> typeRequired, Map<Class<T>, T> injectables) {
        MetaProvider provider = null;

        for (MetaProvider metaProvider : providers) {
            Collection<Class<?>> compatible = metaProvider.getCompatibleTypes();

            if (metaProvider.getBuildType() != controller.getMessageType())
                continue;

            if (compatible.contains(typeRequired)) {
                provider = metaProvider;
                break;
            }

            if (compatible.stream().anyMatch(c -> c.isAssignableFrom(typeRequired)))
                provider = metaProvider;
        }

        if (provider == null)
            throw new AdapterRequiredException("ResponseProvider required for type " + typeRequired + ".");

        Injector child = injector.createChildInjector(new AbstractModule() {

            @Override
            protected void configure() {
            injectables.forEach((key, value) -> bind(key).toInstance(value));
            }
        });

        return child.getInstance(provider.getProviderType());
    }
}
