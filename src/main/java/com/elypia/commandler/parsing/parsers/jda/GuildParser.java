package com.elypia.commandler.parsing.parsers.jda;

import com.elypia.commandler.data.SearchScope;
import com.elypia.commandler.events.CommandEvent;
import com.elypia.commandler.parsing.IParamParser;
import net.dv8tion.jda.core.entities.*;

import java.util.*;

public class GuildParser implements IParamParser<Guild> {

    @Override
    public Guild parse(CommandEvent event, SearchScope scope, String input) {
        final Collection<Guild> guilds = new ArrayList<>();

        switch (scope) {
            case GLOBAL:
                guilds.addAll(event.getMessageEvent().getJDA().getGuilds());
                break;

            case MUTUAL:
                User user = event.getMessage().getAuthor();
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

        event.invalidate("Parameter `" + input + "` could not be be linked to a guild.");
        return null;
    }
}
