package com.elypia.commandler.parsing.parsers.jda;

import com.elypia.commandler.data.SearchScope;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.parsing.impl.JDAParser;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

import java.util.ArrayList;
import java.util.Collection;

public class TextChannelParser extends JDAParser<TextChannel> {

    public TextChannelParser(JDA jda) {
        super(jda);
    }

    @Override
    public TextChannel parse(MessageEvent event, SearchScope scope, String input) throws IllegalArgumentException {
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
