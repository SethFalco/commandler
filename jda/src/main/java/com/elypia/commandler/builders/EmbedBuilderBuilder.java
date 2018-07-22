package com.elypia.commandler.builders;

import com.elypia.commandler.*;
import com.elypia.commandler.impl.*;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;

/**
 * This is a builder so should a user desire to perform
 * different functionality they are able to overwrite it.
 */
public class EmbedBuilderBuilder implements IJDABuilder<EmbedBuilder> {

    @Override
    public Message buildAsEmbed(JDACommand event, EmbedBuilder output) {
        GenericMessageEvent jdaEvent = event.getSource();

        if (jdaEvent.getChannelType().isGuild())
            output.setColor(jdaEvent.getGuild().getSelfMember().getColor());

        return new MessageBuilder(output.build()).build();
    }

    @Override
    public Message build(ICommandEvent<?, ?, Message> event, EmbedBuilder input) {
        return null;
    }
}
