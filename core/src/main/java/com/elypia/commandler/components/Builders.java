package com.elypia.commandler.components;

import com.elypia.commandler.*;
import com.elypia.commandler.impl.*;
import org.slf4j.*;

import java.util.*;

/**
 * The builder is used to build messages from returning commands
 * or the {@link CommandEvent#reply(Object)} method. Builders
 * that implement {@link IBuilder} can be registered via the
 * {@link #registerBuilder(IBuilder, Class[])} method, this tells
 * {@link Commandler} how to turn various types of Objects into
 * messages to be sent in chat.
 *
 * @param <M> The data-type we're building messages into, this
 *            should be the data-type the client of our integrating
 *            platform expects us to send back to users.
 */

public class Builders<M> implements Iterable<IBuilder<?, M>> {

    /**
     * This is thrown with an {@link IllegalArgumentException} if there is no registered
     * {@link IBuilder} with the data type of the object being built.
     */

    private static final String NO_VALID_BUILDER = "No builder implementation registered for data type %s.";

    /**
     * This is thrown with a {@link IllegalStateException} when the
     * {@link IBuilder#build(CommandEvent, Object)} returns null. <br>
     * The default build method is the fallback and should always return a value.
     */

    private static final String NULL_DEFAULT_BUILDER = "String builder %s for data-type %s returned null.";

    /**
     * This is logged whenever a new {@link IBuilder} is registered to a data-type.
     */

    private static final String REGISTERED_BUILDER = "Registered builder {} for data-type {}.";

    /**
     * This is logged when an existing {@link IBuilder} is replaced with
     * a new one. <br> This isn't bad, it's just a warning for the developer.
     */

    private static final String REPLACED_REGISTERED = "Replaced registered builder for {} ({}) with {}.";

    /**
     * We're using SLF4J to manage logging, remember to use a binding / implementation
     * and configure logging for when testing or running an application.
     */

    private static final Logger logger = LoggerFactory.getLogger(Builders.class);

    /**
     * All registered {@link IBuilder} instances mapped
     * to the data type it builds for.
     */

    private Map<Class<?>, IBuilder<?, M>> builders;

    public Builders() {
        builders = new HashMap<>();
    }

    /**
     * Register a new {@link IBuilder} so {@link Commandler} will know
     * how to parse it when a command wishes to send it. <br>
     * If a {@link IBuilder} for this type is already registered
     * then it is replaced with the new {@link IBuilder}.
     *
     * @param newBuilder The {@link IBuilder} that builds an object into a message.
     * @param types The data types this builder can build.
     */

    public void registerBuilder(IBuilder<?, M> newBuilder, Class...types) {
        for (Class type : types) {
            IBuilder<?, M> oldBuilder = builders.put(type, newBuilder);

            if (oldBuilder == null)
                logger.debug(REGISTERED_BUILDER, newBuilder.getClass().getName(), type.getName());
            else
                logger.info(REPLACED_REGISTERED, oldBuilder.getClass().getName(), type.getName(), newBuilder.getClass().getName());
        }
    }

    /**
     * Build an object into the {@link M message} using
     * the respective {@link IBuilder}. <br>
     * When integrating with a platform, you may wish to extend
     * and {@link Override} the {@link #buildMessage(CommandEvent, Object)}
     * method to accomodate platform specific requirements.
     *
     * @param event The source event that triggered this.
     * @param object The user input after already being parsed by the {@link Parsers}.
     * @return A built message ready to send to the client.
     */

    public M buildMessage(CommandEvent<?, ?, M> event, Object object) {
        Objects.requireNonNull(object);
        IBuilder builder = getBuilder(object.getClass());

        M content = (M)builder.build(event, object);

        if (content == null) {
            String builderName = builder.getClass().getName();
            String objectName = object.getClass().getName();
            throw new IllegalStateException(String.format(NULL_DEFAULT_BUILDER, builderName, objectName));
        }

        return content;
    }

    /**
     * Go through the builders and find the most appropriate builder
     * if one is available for building an message from this
     * data-type.
     *
     * @param clazz The data-type we need to build from.
     * @return The builder to convert this to a message.
     * @throws IllegalArgumentException If no {@link IBuilder} is
     *         registered for this data-type.
     */

    protected IBuilder<?, M> getBuilder(Class<?> clazz) {
        if (builders.containsKey(clazz))
            return builders.get(clazz);

        for (Map.Entry<Class<?>, IBuilder<?, M>> entry : builders.entrySet()) {
            if (entry.getKey().isAssignableFrom(clazz))
                return entry.getValue();
        }

        throw new IllegalArgumentException(String.format(NO_VALID_BUILDER, clazz.getName()));
    }

    @Override
    public Iterator<IBuilder<?, M>> iterator() {
        return builders.values().iterator();
    }
}
