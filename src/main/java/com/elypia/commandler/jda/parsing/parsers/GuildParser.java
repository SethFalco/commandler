package com.elypia.commandler.jda.parsing.parsers;

import com.elypia.commandler.data.SearchScope;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.jda.parsing.parsers.impl.JDAParser;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;

import java.util.Collection;

public class GuildParser extends JDAParser<Guild> {

    public GuildParser(JDA jda) {
        super(jda);
    }

    @Override
    public Guild parse(MessageEvent event, String input, SearchScope scope) throws IllegalArgumentException {
    Collection<Guild> guilds = jda.getMutualGuilds();

        for (Guild g : guilds) {
            if (g.getId().equals(input) || g.getName().equalsIgnoreCase(input))
                return g;
        }

        throw new IllegalArgumentException("Parameter `" + input + "` could not be be linked to a guild.");
    }
}
