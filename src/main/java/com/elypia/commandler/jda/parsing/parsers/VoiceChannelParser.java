package com.elypia.commandler.jda.parsing.parsers;

import com.elypia.commandler.jda.data.SearchScope;
import com.elypia.commandler.jda.events.MessageEvent;
import com.elypia.commandler.jda.parsing.parsers.impl.JDAParser;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.*;

import java.util.*;

public class VoiceChannelParser extends JDAParser<VoiceChannel> {

    public VoiceChannelParser(JDA jda) {
        super(jda);
    }

    @Override
    public VoiceChannel parse(MessageEvent event, String input, SearchScope scope) throws IllegalArgumentException {
        final Collection<VoiceChannel> channels = new ArrayList<>();

        switch (scope) {
            case GLOBAL:
                channels.addAll(jda.getVoiceChannels());
                break;

            case MUTUAL:
                User user = event.getMessageEvent().getAuthor();
                Collection<Guild> guilds = user.getMutualGuilds();
                guilds.forEach(g -> channels.addAll(g.getVoiceChannels()));
                break;

            case LOCAL:
                channels.addAll(event.getMessageEvent().getGuild().getVoiceChannels());
                break;
        }

        for (VoiceChannel channel : channels) {
            if (channel.getId().equals(input) || channel.getName().equalsIgnoreCase(input))
                return channel;
        }

        throw new IllegalArgumentException("Parameter `" + input + "` could not be be linked to a channel.");
    }
}
