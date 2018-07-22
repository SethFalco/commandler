package com.elypia.commandler.parsers;

import com.elypia.commandler.*;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;

import java.util.*;

public class TextChannelParser implements IJDAParser<TextChannel> {

    @Override
    public TextChannel parse(JDACommand event, Class<? extends TextChannel> type, String input) {
        Set<TextChannel> channels = new HashSet<>(event.getClient().getTextChannels());
        List<TextChannel> matches = new ArrayList<>();

        channels.forEach(channel -> {
            if (channel.getId().equals(input))
                matches.add(channel);

            else if (channel.getName().equalsIgnoreCase(input))
                matches.add(channel);

            else if (channel.getAsMention().equalsIgnoreCase(input))
                matches.add(channel);

            else if (channel.toString().equalsIgnoreCase(input))
                matches.add(channel);
        });

        return matches.isEmpty() ? null : matches.get(0);
    }
}
