package com.elypia.commandler.builders;

import com.elypia.commandler.events.*;
import com.elypia.commandler.impl.IBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;

/**
 * This is a builder so should a user desire to perform
 * different functionality they are able to overwrite it.
 */

public class MessageEmbedBuilder implements IBuilder<MessageEmbed> {

    @Override
    public MessageEmbed buildAsEmbed(CommandEvent event, MessageEmbed toSend) {
        return toSend;
    }

    @Override
    public String buildAsString(CommandEvent event, MessageEmbed toSend) {
        return null;
    }
}
