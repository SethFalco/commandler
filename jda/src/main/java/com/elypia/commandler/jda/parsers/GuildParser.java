package com.elypia.commandler.jda.parsers;

import com.elypia.commandler.jda.*;
import net.dv8tion.jda.core.entities.Guild;

import java.util.*;

public class GuildParser implements IJDAParser<Guild> {

    @Override
    public Guild parse(JDACommand event, Class<? extends Guild> type, String input) {
        Set<Guild> guilds = new HashSet<>(event.getClient().getGuilds());
        List<Guild> matches = new ArrayList<>();

        guilds.forEach(guild -> {
            if (guild.getId().equals(input))
                matches.add(guild);

            else if (guild.getName().equalsIgnoreCase(input))
                matches.add(guild);

            else if (guild.toString().equalsIgnoreCase(input))
                matches.add(guild);
        });

        return matches.isEmpty() ? null : matches.get(0);
    }
}
