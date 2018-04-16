package com.elypia.commandler.jda.parsing.parsers;

import com.elypia.commandler.parsing.impl.Parser;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.VoiceChannel;

import java.util.Collection;

public class VoiceChannelParser implements Parser<VoiceChannel> {

    private JDA jda;

    public VoiceChannelParser(JDA jda) {
        this.jda = jda;
    }

    @Override
    public VoiceChannel parse(String input) throws IllegalArgumentException {
        Collection<VoiceChannel> channels = jda.getVoiceChannels();

        for (VoiceChannel channel : channels) {
            if (channel.getId().equals(input) || channel.getName().equalsIgnoreCase(input))
                return channel;
        }

        throw new IllegalArgumentException("Parameter `" + input + "` could not be be linked to a channel.");
    }
}
