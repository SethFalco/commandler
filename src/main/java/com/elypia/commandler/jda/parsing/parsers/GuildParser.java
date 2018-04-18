package com.elypia.commandler.jda.parsing.parsers;

import com.elypia.commandler.data.SearchScope;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.jda.parsing.parsers.impl.JDAParser;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.*;

import java.util.*;

public class GuildParser extends JDAParser<Guild> {

    public GuildParser(JDA jda) {
        super(jda);
    }

    @Override
    public Guild parse(MessageEvent event, String input, SearchScope scope) throws IllegalArgumentException {
        final Collection<Guild> guilds = new ArrayList<>();

        switch (scope) {
            case GLOBAL:
                guilds.addAll(jda.getGuilds());
                break;

            case MUTUAL:
                User user = event.getMessageEvent().getAuthor();
                guilds.addAll(user.getMutualGuilds());
                break;

            case LOCAL:
                guilds.add(event.getMessageEvent().getGuild());
                break;
        }

        for (Guild g : guilds) {
            if (g.getId().equals(input) || g.getName().equalsIgnoreCase(input))
                return g;
        }

        throw new IllegalArgumentException("Parameter `" + input + "` could not be be linked to a guild.");
    }
}
