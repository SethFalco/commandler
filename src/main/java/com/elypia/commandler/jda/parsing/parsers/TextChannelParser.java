package com.elypia.commandler.jda.parsing.parsers;

import com.elypia.commandler.parsing.impl.Parser;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.Collection;

public class TextChannelParser implements Parser<TextChannel> {

    private JDA jda;

    public TextChannelParser(JDA jda) {
        this.jda = jda;
    }

    @Override
    public TextChannel parse(String input) throws IllegalArgumentException {
        Collection<TextChannel> channels = jda.getTextChannels();

        for (TextChannel channel : channels) {
            if (channel.getId().equals(input) || channel.getName().equalsIgnoreCase(input) || channel.getAsMention().equals(input))
                return channel;
        }

        throw new IllegalArgumentException("Parameter `" + input + "` could not be be linked to a channel.");
    }
}
