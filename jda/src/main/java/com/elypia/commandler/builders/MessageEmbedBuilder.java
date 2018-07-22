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
public class MessageEmbedBuilder implements IJDABuilder<MessageEmbed> {

    @Override
    public Message buildAsEmbed(CommandEvent<JDA, GenericMessageEvent, Message> event, MessageEmbed output) {
        return new MessageBuilder(output).build();
    }

    @Override
    public Message build(CommandEvent event, MessageEmbed input) {
        return null;
    }
}
