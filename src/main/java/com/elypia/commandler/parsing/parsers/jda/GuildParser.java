package com.elypia.commandler.parsing.parsers.jda;

import com.elypia.commandler.data.SearchScope;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.parsing.ParamParser;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;

import java.util.ArrayList;
import java.util.Collection;

public class GuildParser implements ParamParser<Guild> {

    @Override
    public Guild parse(MessageEvent event, SearchScope scope, String input) throws IllegalArgumentException {
        final Collection<Guild> guilds = new ArrayList<>();

        switch (scope) {
            case GLOBAL:
                guilds.addAll(event.getMessageEvent().getJDA().getGuilds());
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
