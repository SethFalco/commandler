package com.elypia.commandler.parsers;

import com.elypia.commandler.data.SearchScope;
import com.elypia.commandler.CommandEvent;
import com.elypia.commandler.impl.IParamParser;
import net.dv8tion.jda.core.entities.*;

import java.util.*;

public class VoiceChannelParser implements IParamParser<VoiceChannel> {

    @Override
    public VoiceChannel parse(CommandEvent event, SearchScope scope, String input) {
        final Collection<VoiceChannel> channels = new ArrayList<>();

        switch (scope) {
            case GLOBAL:
                channels.addAll(event.getMessageEvent().getJDA().getVoiceChannels());
                break;

            case MUTUAL:
                User user = event.getMessage().getAuthor();
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

        return null;
    }
}
