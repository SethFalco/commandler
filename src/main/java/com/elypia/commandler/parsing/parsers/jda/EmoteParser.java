package com.elypia.commandler.parsing.parsers.jda;

import com.elypia.commandler.data.SearchScope;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.parsing.impl.JDAParser;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class EmoteParser extends JDAParser<Emote> {

    public EmoteParser(JDA jda) {
        super(jda);
    }

    @Override
    public Emote parse(MessageEvent event, SearchScope scope, String input) throws IllegalArgumentException {
        final Set<Emote> emotes = new HashSet<>();

        if (event != null)
            emotes.addAll(event.getMessageEvent().getMessage().getEmotes());

        switch (scope) {
            case GLOBAL:
                emotes.addAll(jda.getEmotes());
                break;

            case MUTUAL:
                User user = event.getMessageEvent().getAuthor();
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
