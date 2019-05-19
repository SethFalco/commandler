package com.elypia.commandler.metadata;

import com.elypia.commandler.exceptions.NoBuilderException;
import com.elypia.commandler.inject.ServiceProvider;
import com.elypia.commandler.interfaces.Builder;
import com.elypia.commandler.interfaces.CommandlerEvent;
import com.elypia.commandler.metadata.data.BuilderData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

/**
 * The builder is used to load messages from returning commands
 * or the {@link CommandlerEvent#send(Object)} method.
 *
 * @param <M> The data-type we're building messages into, this
 *            should be the data-type the client of our integrating
 *            platform expects us to send back to users.
 */
public class ResponseBuilder<M> {

    /**
     * We're using SLF4J to manage logging, remember to use a binding / implementation
     * and configure logging for when testing or running an application.
     */
    private static final Logger logger = LoggerFactory.getLogger(ResponseBuilder.class);

    private final ServiceProvider provider;

    /**
     * All registered {@link Builder} instances.
     */
    private Collection<BuilderData> builders;

    public ResponseBuilder(ServiceProvider provider, Collection<BuilderData> builders) {
        this.provider = provider;
        this.builders = builders;
    }

    /**
     * Build an object into the {@link M message} using
     * the respective {@link Builder}. <br>
     * When integrating with a platform, you may wish to extend
     * and {@link Override} the load
     * method to accomodate platform specific requirements.
     *
     * @param event The source event that triggered this.
     * @param object The user input after already being parsed by the {@link ParameterParser}.
     * @return A built message ready to send to the client.
     */
    public M build(CommandlerEvent<?, M> event, Object object) {
        Objects.requireNonNull(object);
        Builder builder = getBuilder(object.getClass());

        M content = (M)builder.build(event, object);

        if (content == null) {
            String builderName = builder.getClass().getName();
            String objectName = object.getClass().getName();
            throw new IllegalStateException(String.format("String builder %s for data-type %s returned null.", builderName, objectName));
        }

        return content;
    }

    /**
     * Go through the builders and find the most appropriate builder
     * if one is available for building an message from this
     * data-type.
     *
     * @param typeRequired The data-type we need to load from.
     * @return The builder to convert this to a message.
     * @throws IllegalArgumentException If no {@link Builder} is
     * registered for this data-type.
     */
    protected Builder<?, ?, M> getBuilder(Class<?> typeRequired) {
        Optional<BuilderData> builder = builders.parallelStream()
            .filter((p) -> p.getCompatible().contains(typeRequired))
            .findAny();

        if (!builder.isPresent()) {
            builder = builders.parallelStream()
                .filter((p) -> p.getCompatible().parallelStream()
                    .anyMatch((c) -> c.isAssignableFrom(typeRequired)))
                .findAny();
        }

        if (!builder.isPresent())
            throw new NoBuilderException("No builder implementation registered for data type %s.", typeRequired);

        return provider.get(builder.get().getBuilderClass());
    }
}
