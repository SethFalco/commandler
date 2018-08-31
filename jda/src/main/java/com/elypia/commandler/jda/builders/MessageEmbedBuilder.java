package com.elypia.commandler.jda.builders;

import com.elypia.commandler.jda.*;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.*;

/**
 * This is a builder so should a user desire to perform
 * different functionality they are able to overwrite it.
 */
public class MessageEmbedBuilder implements IJDABuilder<MessageEmbed> {

    @Override
    public Message buildEmbed(JDACommand event, MessageEmbed output) {
        return new MessageBuilder(output).build();
    }

    @Override
    public Message build(JDACommand event, MessageEmbed output) {
        return new MessageBuilder(output).build();
    }
}
