package com.elypia.commandler.parsing.parsers.jda;

import com.elypia.commandler.data.SearchScope;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.parsing.ParamParser;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;

import java.util.ArrayList;
import java.util.Collection;

public class VoiceChannelParser implements ParamParser<VoiceChannel> {

    @Override
    public VoiceChannel parse(MessageEvent event, SearchScope scope, String input) throws IllegalArgumentException {
        final Collection<VoiceChannel> channels = new ArrayList<>();

        switch (scope) {
            case GLOBAL:
                channels.addAll(event.getMessageEvent().getJDA().getVoiceChannels());
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
