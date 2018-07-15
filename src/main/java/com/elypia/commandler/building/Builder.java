package com.elypia.commandler.building;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.builders.*;
import com.elypia.commandler.builders.StringBuilder;
import com.elypia.commandler.building.builders.*;
import com.elypia.commandler.events.CommandEvent;
import com.elypia.commandler.impl.*;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.*;
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

public class Builder {

    /**
     * This is thrown with an {@link IllegalArgumentException} if it is
     * attempted to register a builder for {@link Message}. This can't be
     * registered as it is already a built message.
     */

    private static final String REGISTER_MESSAGE = "Can't register a builder for Message as that's already a built message.";

    /**
     * This is thrown with an {@link IllegalArgumentException} if there is no registered
     * {@link IBuilder} with the data type of the object being built.
     */

    private static final String NO_VALID_BUILDER = "No builder implementation registered for data type %s.";

    /**
     * This is thrown with a {@link IllegalStateException} when the
     * {@link IBuilder#buildAsString(CommandEvent, Object)} returns null.
     */

    private static final String NULL_STRING_BUILDER = "String builder %s for data type %s returned null.";

    /**
     * This is logged when {@link Commandler} registers default {@link IBuilder builders}.
     */

    private static final String DEFAULTS_REGISTERED = "Registered builder for type {} with new instance of {} by default.";

    /**
     * This is logged when an existing {@link IBuilder} is replaced with
     * a new one.
     */

    private static final String REPLACED_REGISTERED = "Replaced registered builder for {} ({}) with {}.";

    private static final Logger logger = LoggerFactory.getLogger(Builder.class);

    /**
     * The parent {@link Commandler} instance that this
     * {@link Builder} is registered to.
     */

    private Commandler commandler;

    /**
     * The configuration used to manager this {@link #commandler} instance.
     */

    private IConfiler confiler;

    /**
     * All registered {@link IBuilder} instances mapped
     * to the data type it builds for.
     */

    private Map<Class<?>, IBuilder<?>> builders;

    public Builder(Commandler commandler) {
        this.commandler = Objects.requireNonNull(commandler);
        confiler = commandler.getConfiler();

        builders = new HashMap<>();

        // ? Register default builders, users can overwrite these if required.
        registerBuilder(EmbedBuilder.class, new EmbedBuilderBuilder());
        registerBuilder(String.class, new StringBuilder());
        registerBuilder(MessageEmbed.class, new MessageEmbedBuilder());

        builders.forEach((type, build) -> {
            String typeName = type.getName();
            String buildName = build.getClass().getName();
            logger.debug(DEFAULTS_REGISTERED, typeName, buildName);
        });
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

    public <T> void registerBuilder(Class<T> t, IBuilder<T> newBuilder) {
        if (t == Message.class)
            throw new IllegalArgumentException(REGISTER_MESSAGE);

        IBuilder<?> oldBuilder = builders.put(t, newBuilder);

        // ? Occurs when a builder for a data type was replaced.
        if (oldBuilder != null) {
            String typeName = t.getName();
            String oldClass = oldBuilder.getClass().getName();
            String newClass = newBuilder.getClass().getName();
            logger.info(REPLACED_REGISTERED, typeName, oldClass, newClass);
        }
    }

    public Message buildMessage(CommandEvent event, Object object) {
        Objects.requireNonNull(object);

        // ? If it's a message, just send it; else find the appropriate builder.
        if (object instanceof Message)
            return (Message)object;

        Message message = event.getMessage();
        IBuilder builder = builders.get(object.getClass());

        if (builder == null) {
            String objectName = object.getClass().getName();
            throw new IllegalArgumentException(String.format(NO_VALID_BUILDER, objectName));
        }

        MessageEmbed embed = builder.buildAsEmbed(event, object);

        if (embed != null) {
            if (!message.getChannelType().isGuild())
                return new MessageBuilder(embed).build();

            Member self = message.getGuild().getSelfMember();

            if (self.hasPermission(message.getTextChannel(), Permission.MESSAGE_EMBED_LINKS))
                return new MessageBuilder(embed).build();
        }

        String content = builder.buildAsString(event, object);

        if (content == null) {
            String builderName = builder.getClass().getName();
            String objectName = object.getClass().getName();
            throw new IllegalStateException(String.format(NULL_STRING_BUILDER, builderName, objectName));
        }

        return new MessageBuilder(content).build();

    }

    public Commandler getCommandler() {
        return commandler;
    }

    public IConfiler getConfiler() {
        return confiler;
    }
}
