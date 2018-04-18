package com.elypia.commandler.jda.parsing.parsers;

import com.elypia.commandler.data.SearchScope;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.jda.parsing.parsers.impl.JDAParser;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.*;

import java.util.*;

public class TextChannelParser extends JDAParser<TextChannel> {

    public TextChannelParser(JDA jda) {
        super(jda);
    }

    @Override
    public TextChannel parse(MessageEvent event, String input, SearchScope scope) throws IllegalArgumentException {
        final Collection<TextChannel> channels = new ArrayList<>();

        switch (scope) {
            case GLOBAL:
                channels.addAll(jda.getTextChannels());
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
