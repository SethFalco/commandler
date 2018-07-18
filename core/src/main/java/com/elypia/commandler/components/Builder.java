package com.elypia.commandler.components;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.CommandEvent;
import com.elypia.commandler.impl.*;
import org.slf4j.*;

import java.util.*;

/**
 * The builder is used to build messages from returns commands
 * or the {@link CommandEvent#reply(Object)} method. Builders
 * that implement {@link IBuilder} can be registered via the
 * {@link #registerBuilder(Class, IBuilder)} method, this tells
 * {@link Commandler} how to turn various types of Objects into
 * messages to be sent in chat.
 */

public class Builder<M> {

    /**
     * This is thrown with an {@link IllegalArgumentException} if there is no registered
     * {@link IBuilder} with the data type of the object being built.
     */

    private static final String NO_VALID_BUILDER = "No builder implementation registered for data type %s.";

    /**
     * This is thrown with a {@link IllegalStateException} when the
     * {@link IBuilder#build(CommandEvent, Object)} returns null.
     */

    private static final String NULL_DEFAULT_BUILDER = "String builder %s for data type %s returned null.";

    /**
     * This is logged when an existing {@link IBuilder} is replaced with
     * a new one.
     */

    private static final String REPLACED_REGISTERED = "Replaced registered builder for {} ({}) with {}.";

    private static final Logger logger = LoggerFactory.getLogger(Builder.class);

    /**
     * All registered {@link IBuilder} instances mapped
     * to the data type it builds for.
     */

    private Map<Class<?>, IBuilder<?, M>> builders;

    public Builder() {
        builders = new HashMap<>();
    }

    /**
     * Register a new {@link IBuilder} so {@link Commandler} will know
     * how to parse it when a command wishes to send it. <br>
     * If a {@link IBuilder} for this {@link T type} is already registered
     * then it is replaced with the new {@link IBuilder}.
     *
     * @param t The data type this builder can build.
     * @param newBuilder The {@link IBuilder} that builds an object into a message.
     * @param <T> Any type of object.
     * @throws IllegalArgumentException If an attempt is made to register
     * a {@link IBuilder} for {@link Message}.
     */

    public <I> void registerBuilder(Class<I> input, IBuilder<I, M> newBuilder) {
        IBuilder<?, M> oldBuilder = builders.put(input, newBuilder);

        // ? Occurs when a builder for a data type was replaced.
        if (oldBuilder != null) {
            String typeName = input.getName();
            String oldClass = oldBuilder.getClass().getName();
            String newClass = newBuilder.getClass().getName();
            logger.info(REPLACED_REGISTERED, typeName, oldClass, newClass);
        }
    }

    public M buildMessage(CommandEvent<?, ?, M> event, Object object) {
        Objects.requireNonNull(object);

        IBuilder builder = builders.get(object.getClass());

        if (builder == null) {
            String objectName = object.getClass().getName();
            throw new IllegalArgumentException(String.format(NO_VALID_BUILDER, objectName));
        }

        M content = (M)builder.build(event, object);

        if (content == null) {
            String builderName = builder.getClass().getName();
            String objectName = object.getClass().getName();
            throw new IllegalStateException(String.format(NULL_DEFAULT_BUILDER, builderName, objectName));
        }

        return content;
    }
}
