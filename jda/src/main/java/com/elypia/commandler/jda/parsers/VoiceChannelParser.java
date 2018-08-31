package com.elypia.commandler.jda.parsers;

import com.elypia.commandler.jda.*;
import net.dv8tion.jda.core.entities.VoiceChannel;

import java.util.*;

public class VoiceChannelParser implements IJDAParser<VoiceChannel> {

    @Override
    public VoiceChannel parse(JDACommand event, Class<? extends VoiceChannel> type, String input) {
        Set<VoiceChannel> channels = new HashSet<>(event.getClient().getVoiceChannels());
        List<VoiceChannel> matches = new ArrayList<>();

        channels.forEach(channel -> {
            if (channel.getId().equals(input))
                matches.add(channel);

            else if (channel.getName().equalsIgnoreCase(input))
                matches.add(channel);

            else if (channel.toString().equalsIgnoreCase(input))
                matches.add(channel);
        });

        return matches.isEmpty() ? null : matches.get(0);
    }
}
