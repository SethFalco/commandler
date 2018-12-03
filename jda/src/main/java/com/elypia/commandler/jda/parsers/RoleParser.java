package com.elypia.commandler.jda.parsers;

import com.elypia.commandler.jda.*;
import com.elypia.commandler.jda.annotations.validation.param.Search;
import net.dv8tion.jda.core.entities.*;

import java.util.*;

public class RoleParser implements IJDAParser<Role> {

    @Override
    public Role parse(JDACommand command, Class<? extends Role> type, String input) {
        Set<Role> roles = getRoles(command, type);
        List<Role> matches = new ArrayList<>();

        roles.forEach(role -> {
            if (role.getAsMention().equalsIgnoreCase(input))
                matches.add(role);

            else if (role.getId().equals(input))
                matches.add(role);

            else if (role.getName().equalsIgnoreCase(input))
                matches.add(role);
        });

        return matches.isEmpty() ? null : matches.get(0);
    }

    public Set<Role> getRoles(JDACommand command, Class<? extends Role> type) {
        Search search = type.getAnnotation(Search.class);
        Scope scope = search == null ? Scope.LOCAL : search.value();
        Set<Role> roles = new HashSet<>();

        switch (scope) {
            case LOCAL:
                roles.addAll(command.getMessage().getGuild().getRoles());
                break;

            case GLOBAL:
                roles.addAll(command.getClient().getRoles());
                break;

            case MUTUAL:
                for (Guild guild : command.getMessage().getAuthor().getMutualGuilds())
                    roles.addAll(guild.getRoles());

            default:
                throw new RuntimeException();
        }

        return roles;
    }
}
