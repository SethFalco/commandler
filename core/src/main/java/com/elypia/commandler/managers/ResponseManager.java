package com.elypia.commandler.managers;

import com.elypia.commandler.CommandlerEvent;
import com.elypia.commandler.exceptions.AdapterRequiredException;
import com.elypia.commandler.interfaces.*;
import com.elypia.commandler.metadata.MetaProvider;
import org.slf4j.*;

import java.util.*;

public class ResponseManager {

    /** SLF4J Logger*/
    private static final Logger logger = LoggerFactory.getLogger(ResponseManager.class);

    /** Used to manage dependency injection when constructions param adapters. */
    private final InjectionManager injectionManager;

    private final Collection<MetaProvider> providers;

    public ResponseManager(InjectionManager injectionManager, MetaProvider... adapters) {
        this(injectionManager, List.of(adapters));
    }

    public ResponseManager(InjectionManager injectionManager, Collection<MetaProvider> providers) {
        this.injectionManager = Objects.requireNonNull(injectionManager);
        this.providers = Objects.requireNonNull(providers);

        for (MetaProvider provider : providers)
            logger.debug("Provider added for type {}.", provider.getProviderType().getSimpleName());
    }

    public <M> M provide(CommandlerEvent<?, ?> event, Object object) {
        CommandlerEvent<?, M> e = (CommandlerEvent<?, M>)event;
        return provide(e, object, e.getController());
    }
    public <M> M provide(CommandlerEvent<?, ?> event, Object object, Controller<M> controller) {
        return provide(event, object, controller, controller.getMessageType());
    }

    /**
     * Build an message object to send back to the client using
     * the respective {@link ResponseProvider}.
     *
     * @param controller The controller that received this event.
     * @param object The user input after already being parsed by the {@link AdapterManager}.
     * @return A built message ready to send to the client.
     */
    public <T> T provide(CommandlerEvent<?, ?> event, Object object, Controller controller, Class<T> type) {
        Objects.requireNonNull(controller);
        Objects.requireNonNull(object);
        ResponseProvider responseProvider = getProvider(controller, object.getClass());

        Object content = responseProvider.provide(event, object);

        if (content == null)
            throw new IllegalStateException(String.format("String adapter `%s`returned null.", responseProvider.getClass().getName()));

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
     * @throws IllegalArgumentException If no {@link ResponseProvider} is
     * registered for this data-type.
     */
    public <T> ResponseProvider<?, ?> getProvider(Controller controller, Class<?> typeRequired) {
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

        return injectionManager.getInjector().getInstance(provider.getProviderType());
    }
}
