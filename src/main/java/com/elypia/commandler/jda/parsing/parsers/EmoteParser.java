package com.elypia.commandler.jda.parsing.parsers;

import com.elypia.commandler.data.SearchScope;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.jda.parsing.parsers.impl.JDAParser;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;

import java.util.ArrayList;
import java.util.Collection;

public class EmoteParser extends JDAParser<Emote> {

    public EmoteParser(JDA jda) {
        super(jda);
    }

    @Override
    public Emote parse(MessageEvent event, String input, SearchScope scope) throws IllegalArgumentException {
        final Collection<Emote> emotes = new ArrayList<>();

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
            if (emote.getId().equals(input) || emote.getAsMention().equals(input) || emote.getName().equalsIgnoreCase(input))
                return emote;
        }

        throw new IllegalArgumentException("Parameter `" + input + "` could not be be linked to an emote.");
    }
}
