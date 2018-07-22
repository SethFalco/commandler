package com.elypia.commandler.builders;

import com.elypia.commandler.*;
import com.elypia.commandler.impl.IBuilder;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;

/**
 * This is a builder so should a user desire to perform
 * different functionality they are able to overwrite it.
 */
public class EmbedBuilderBuilder implements IJDABuilder<EmbedBuilder> {

    @Override
    public Message buildAsEmbed(CommandEvent<JDA, GenericMessageEvent, Message> event, EmbedBuilder output) {
        GenericMessageEvent jdaEvent = event.getSourceEvent();

        if (jdaEvent.getChannelType().isGuild())
            output.setColor(jdaEvent.getGuild().getSelfMember().getColor());

        return new MessageBuilder(output.build()).build();
    }

    @Override
    public Message build(CommandEvent event, EmbedBuilder input) {
        return null;
    }
}
