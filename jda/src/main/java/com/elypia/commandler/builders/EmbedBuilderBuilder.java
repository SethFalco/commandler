package com.elypia.commandler.builders;

import com.elypia.commandler.CommandEvent;
import com.elypia.commandler.impl.IBuilder;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;

/**
 * This is a builder so should a user desire to perform
 * different functionality they are able to overwrite it.
 */

public class EmbedBuilderBuilder implements IBuilder<EmbedBuilder> {

    @Override
    public MessageEmbed buildAsEmbed(CommandEvent event, EmbedBuilder toSend) {
        Message message = event.getMessage();

        if (message.getChannelType().isGuild())
            toSend.setColor(message.getGuild().getSelfMember().getColor());

        return toSend.build();
    }

    @Override
    public String buildAsString(CommandEvent event, EmbedBuilder toSend) {
        return null;
    }
}
