package com.elypia.commandler.sending;

import com.elypia.commandler.*;
import com.elypia.commandler.confiler.Confiler;
import com.elypia.commandler.events.AbstractEvent;
import com.elypia.commandler.sending.builders.EmbedBuilderBuilder;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.*;

import java.util.*;

public class Builder {

    private Commandler commandler;
    private Confiler confiler;

    private Map<Class<?>, IMessageBuilder> builders;

    public Builder(Commandler commandler) {
        this.commandler = Objects.requireNonNull(commandler);
        confiler = commandler.getConfiler();

        builders = new HashMap<>();

        registerBuilder(EmbedBuilder.class, new EmbedBuilderBuilder());
    }

    public <T> void registerBuilder(Class<T> t, IMessageBuilder<T> parser) {
        if (builders.put(t, parser) != null)
            Utils.log("Replaced existing %s with the new sender.", t.getName());
    }

    public Message buildMessage(AbstractEvent event, Object object) {
        if (object instanceof MessageEmbed)
            return new MessageBuilder((MessageEmbed)object).build();

        if (object instanceof Message)
            return (Message)object;

        if (object instanceof String)
            return new MessageBuilder((String)object).build();

        Message message = event.getMessage();
        IMessageBuilder builder = builders.get(object.getClass());
        MessageEmbed embed = builder.buildAsEmbed(event, object);

        if (message.getChannelType().isGuild()) {
            Member self = message.getGuild().getSelfMember();

            if (self.hasPermission(message.getTextChannel(), Permission.MESSAGE_EMBED_LINKS) && embed != null)
                return new MessageBuilder(embed).build();
            else
                return new MessageBuilder(builder.buildAsString(event, object)).build();
        }

        if (embed != null)
            return new MessageBuilder(builder.buildAsEmbed(event, object)).build();
        else
            return new MessageBuilder(builder.buildAsString(event, object)).build();
    }
}

