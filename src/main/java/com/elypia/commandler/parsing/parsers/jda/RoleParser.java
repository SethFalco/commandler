package com.elypia.commandler.parsing.parsers.jda;

import com.elypia.commandler.data.SearchScope;
import com.elypia.commandler.events.CommandEvent;
import com.elypia.commandler.parsing.IParamParser;
import net.dv8tion.jda.core.entities.*;

import java.util.*;

public class RoleParser implements IParamParser<Role> {

    @Override
    public Role parse(CommandEvent event, SearchScope scope, String input) {
        final Collection<Role> roles = new ArrayList<>();

        switch (scope) {
            case GLOBAL:
                roles.addAll(event.getMessageEvent().getJDA().getRoles());
                break;

            case MUTUAL:
                User user = event.getMessage().getAuthor();
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

        event.invalidate("Parameter `" + input + "` could not be be linked to a role.");
        return null;
    }
}
