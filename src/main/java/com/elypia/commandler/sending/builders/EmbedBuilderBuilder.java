package com.elypia.commandler.sending.builders;

import com.elypia.commandler.events.AbstractEvent;
import com.elypia.commandler.sending.IMessageBuilder;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;

public class EmbedBuilderBuilder implements IMessageBuilder<EmbedBuilder> {

    @Override
    public MessageEmbed buildAsEmbed(AbstractEvent event, EmbedBuilder toSend) {
        Message message = event.getMessage();

        if (message.getChannelType().isGuild())
            toSend.setColor(message.getGuild().getSelfMember().getColor());

        return toSend.build();
    }

    @Override
    public MessageEmbed buildAsEmbed(AbstractEvent event, EmbedBuilder... toSend) {
        return null;
    }

    @Override
    public String buildAsString(AbstractEvent event, EmbedBuilder toSend) {
        return null;
    }

    @Override
    public String buildAsString(AbstractEvent event, EmbedBuilder... toSend) {
        return null;
    }
}
