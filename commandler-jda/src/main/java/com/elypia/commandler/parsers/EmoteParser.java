package com.elypia.commandler.parsers;

import com.elypia.commandler.data.SearchScope;
import com.elypia.commandler.events.CommandEvent;
import com.elypia.commandler.impl.IParamParser;
import net.dv8tion.jda.core.entities.*;

import java.util.*;

public class EmoteParser implements IParamParser<Emote> {

    @Override
    public Emote parse(CommandEvent event, SearchScope scope, String input) {
        final Set<Emote> emotes = new HashSet<>();

        emotes.addAll(event.getMessage().getEmotes());

        switch (scope) {
            case GLOBAL:
                emotes.addAll(event.getMessageEvent().getJDA().getEmotes());
                break;

            case MUTUAL:
                User user = event.getMessage().getAuthor();
                Collection<Guild> guilds = user.getMutualGuilds();
                guilds.forEach(g -> emotes.addAll(g.getEmotes()));
                break;

            case LOCAL:
                emotes.addAll(event.getMessageEvent().getGuild().getEmotes());
                break;
        }

        for (Emote emote : emotes) {
            if (compare(input, emote))
                return emote;
        }

        return null;
    }

    public boolean compare(String input, Emote emote) {
        return (emote.getId().equals(input) || emote.getAsMention().equalsIgnoreCase(input) || emote.getName().equalsIgnoreCase(input) || emote.toString().equalsIgnoreCase(input));
    }
}
