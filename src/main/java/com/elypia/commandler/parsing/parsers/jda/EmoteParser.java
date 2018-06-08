package com.elypia.commandler.parsing.parsers.jda;

import com.elypia.commandler.data.SearchScope;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.parsing.IParamParser;
import net.dv8tion.jda.core.entities.*;

import java.util.*;

public class EmoteParser implements IParamParser<Emote> {

    @Override
    public Emote parse(MessageEvent event, SearchScope scope, String input) throws IllegalArgumentException {
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

        throw new IllegalArgumentException("Parameter `" + input + "` could not be be linked to an emote.");
    }

    public boolean compare(String input, Emote emote) {
        return (emote.getId().equals(input) || emote.getAsMention().equalsIgnoreCase(input) || emote.getName().equalsIgnoreCase(input) || emote.toString().equalsIgnoreCase(input));
    }
}
