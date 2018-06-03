package com.elypia.commandler.parsing.parsers.jda;

import com.elypia.commandler.data.SearchScope;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.parsing.IParamParser;
import net.dv8tion.jda.core.entities.*;

import java.util.*;

public class TextChannelParser implements IParamParser<TextChannel> {

    @Override
    public TextChannel parse(MessageEvent event, SearchScope scope, String input) throws IllegalArgumentException {
        final Collection<TextChannel> channels = new ArrayList<>();

        switch (scope) {
            case GLOBAL:
                channels.addAll(event.getMessageEvent().getJDA().getTextChannels());
                break;

            case MUTUAL:
                User user = event.getMessageEvent().getAuthor();
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

        throw new IllegalArgumentException("Parameter `" + input + "` could not be be linked to a channel.");
    }
}
