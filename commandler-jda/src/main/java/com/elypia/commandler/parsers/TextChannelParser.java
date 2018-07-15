package com.elypia.commandler.parsers;

import com.elypia.commandler.data.SearchScope;
import com.elypia.commandler.events.CommandEvent;
import com.elypia.commandler.impl.IParamParser;
import net.dv8tion.jda.core.entities.*;

import java.util.*;

public class TextChannelParser implements IParamParser<TextChannel> {

    @Override
    public TextChannel parse(CommandEvent event, SearchScope scope, String input) {
        final Collection<TextChannel> channels = new ArrayList<>();

        switch (scope) {
            case GLOBAL:
                channels.addAll(event.getMessageEvent().getJDA().getTextChannels());
                break;

            case MUTUAL:
                User user = event.getMessage().getAuthor();
                Collection<Guild> guilds = user.getMutualGuilds();
                guilds.forEach(g -> channels.addAll(g.getTextChannels()));
                break;

            case LOCAL:
                channels.addAll(event.getMessageEvent().getGuild().getTextChannels());
                break;
        }

        for (TextChannel channel : channels) {
            if (channel.getId().equals(input) || channel.getName().equalsIgnoreCase(input) || channel.getAsMention().equals(input))
                return channel;
        }

        return null;
    }
}
