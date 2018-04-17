package com.elypia.commandler.jda.parsing.parsers;

import com.elypia.commandler.data.SearchScope;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.jda.parsing.parsers.impl.JDAParser;
import com.elypia.commandler.parsing.impl.Parser;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.Collection;

public class TextChannelParser extends JDAParser<TextChannel> {

    public TextChannelParser(JDA jda) {
        super(jda);
    }

    @Override
    public TextChannel parse(MessageEvent event, String input, SearchScope scope) throws IllegalArgumentException {
        Collection<TextChannel> channels = jda.getTextChannels();

        for (TextChannel channel : channels) {
            if (channel.getId().equals(input) || channel.getName().equalsIgnoreCase(input) || channel.getAsMention().equals(input))
                return channel;
        }

        throw new IllegalArgumentException("Parameter `" + input + "` could not be be linked to a channel.");
    }
}
