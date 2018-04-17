package com.elypia.commandler.jda.parsing.parsers;

import com.elypia.commandler.data.SearchScope;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.jda.parsing.parsers.impl.JDAParser;
import com.elypia.commandler.parsing.impl.Parser;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.VoiceChannel;

import java.util.Collection;

public class VoiceChannelParser extends JDAParser<VoiceChannel> {

    public VoiceChannelParser(JDA jda) {
        super(jda);
    }

    @Override
    public VoiceChannel parse(MessageEvent event, String input, SearchScope scope) throws IllegalArgumentException {
        Collection<VoiceChannel> channels = jda.getVoiceChannels();

        for (VoiceChannel channel : channels) {
            if (channel.getId().equals(input) || channel.getName().equalsIgnoreCase(input))
                return channel;
        }

        throw new IllegalArgumentException("Parameter `" + input + "` could not be be linked to a channel.");
    }
}
