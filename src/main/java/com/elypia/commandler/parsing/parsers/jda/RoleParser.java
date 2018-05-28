package com.elypia.commandler.parsing.parsers.jda;

import com.elypia.commandler.data.SearchScope;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.parsing.ParamParser;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;

import java.util.ArrayList;
import java.util.Collection;

public class RoleParser implements ParamParser<Role> {

    @Override
    public Role parse(MessageEvent event, SearchScope scope, String input) throws IllegalArgumentException {
        final Collection<Role> roles = new ArrayList<>();

        switch (scope) {
            case GLOBAL:
                roles.addAll(event.getMessageEvent().getJDA().getRoles());
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
