package com.elypia.commandler.jda.parsing.parsers;

import com.elypia.commandler.jda.data.SearchScope;
import com.elypia.commandler.jda.events.MessageEvent;
import com.elypia.commandler.jda.parsing.parsers.impl.JDAParser;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.*;

import java.util.*;

public class RoleParser extends JDAParser<Role> {

    public RoleParser(JDA jda) {
        super(jda);
    }

    @Override
    public Role parse(MessageEvent event, String input, SearchScope scope) throws IllegalArgumentException {
        final Collection<Role> roles = new ArrayList<>();

        switch (scope) {
            case GLOBAL:
                roles.addAll(jda.getRoles());
                break;

            case MUTUAL:
                User user = event.getMessageEvent().getAuthor();
                Collection<Guild> guilds = user.getMutualGuilds();
                guilds.forEach(g -> roles.addAll(g.getRoles()));
                break;

            case LOCAL:
                roles.addAll(event.getMessageEvent().getGuild().getRoles());
        }

        for (Role role : roles) {
            if (role.getId().equals(input) || role.getAsMention().equals(input) || role.getName().equalsIgnoreCase(input))
                return role;
        }

        throw new IllegalArgumentException("Parameter `" + input + "` could not be be linked to a role.");
    }
}
