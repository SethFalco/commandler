package com.elypia.commandler;

import com.elypia.commandler.annotations.Compatible;
import com.elypia.commandler.interfaces.*;
import org.slf4j.*;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * The builder is used to build messages from returning commands
 * or the {@link ICommandEvent#send(Object)} method. Builders
 * that implement {@link IBuilder} can be registered via the
 * {@link #add method, this tells
 * {@link Commandler} how to turn various types of Objects into
 * messages to be sent in chat.
 *
 * @param <M> The data-type we're building messages into, this
 *            should be the data-type the client of our integrating
 *            platform expects us to send back to users.
 */
public class MessageBuilder<M> {

    /**
     * We're using SLF4J to manage logging, remember to use a binding / implementation
     * and configure logging for when testing or running an application.
     */
    protected static final Logger logger = LoggerFactory.getLogger(MessageBuilder.class);

    /**
     * All registered {@link IBuilder} instances mapped
     * to the data type it builds for.
     */
    private Map<Class<? extends IBuilder<?, ?, M>>, IBuilder<?, ?, M>> builders;

    public MessageBuilder() {
        builders = new HashMap<>();
    }

    /**
     * Register a new {@link IBuilder} so {@link Commandler} will know
     * how to parse it when a command wishes to send it. <br>
     * If a {@link IBuilder} for this type is already registered
     * then it is replaced with the new {@link IBuilder}.
     *
     * @param types The data types this builder can build.
     */
    @SafeVarargs
    final public void add(Class<? extends IBuilder<?, ?, M>>... types) {
        for (Class<? extends IBuilder<?, ?, M>> type : types)
            builders.putIfAbsent(type, null);
    }

    /**
     * Build an object into the {@link M message} using
     * the respective {@link IBuilder}. <br>
     * When integrating with a platform, you may wish to extend
     * and {@link Override} the build
     * method to accomodate platform specific requirements.
     *
     * @param event The source event that triggered this.
     * @param object The user input after already being parsed by the {@link ParameterParser}.
     * @return A built message ready to send to the client.
     */
    public M build(ICommandEvent<?, ?, M> event, Object object) {
        Objects.requireNonNull(object);
        IBuilder builder = getBuilder(event, object.getClass());

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
     * @param typeRequired The data-type we need to build from.
     * @return The builder to convert this to a message.
     * @throws IllegalArgumentException If no {@link IBuilder} is
     *         registered for this data-type.
     */
    private IBuilder<?, ?, M> getBuilder(ICommandEvent<?, ?, M> event, Class<?> typeRequired) {
        for (var builder : event.getInput().getModuleData().getBuilders()) {
            Class<?>[] compatibleTypes = builder.getAnnotation(Compatible.class).value();

            for (Class<?> type : compatibleTypes) {
                if (type == typeRequired || type.isAssignableFrom(typeRequired)) {
                    if (!builders.containsKey(builder)) {
                        try {
                            builders.put((Class<IBuilder<?, ?, M>>)builder, builder.getConstructor().newInstance());
                        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | InstantiationException e) {
                            throw new IllegalStateException("No default constructor.");
                        }
                    }

                    return builders.get(builder);
                }
            }
        }

        throw new IllegalArgumentException(String.format("No builder implementation registered for data type %s.", typeRequired.getName()));
    }
}
