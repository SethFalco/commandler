package com.elypia.commandler.jda.parsers;

import com.elypia.commandler.jda.*;
import net.dv8tion.jda.core.entities.Role;

import java.util.*;

public class RoleParser implements IJDAParser<Role> {

    @Override
    public Role parse(JDACommand event, Class<? extends Role> type, String input) {
        Set<Role> roles = new HashSet<>(event.getClient().getRoles());
        List<Role> matches = new ArrayList<>();

        roles.forEach(role -> {
            if (role.getId().equals(input))
                matches.add(role);

            else if (role.getName().equalsIgnoreCase(input))
                matches.add(role);

            else if (role.getAsMention().equalsIgnoreCase(input))
                matches.add(role);

            else if (role.toString().equalsIgnoreCase(input))
                matches.add(role);
        });

        return matches.isEmpty() ? null : matches.get(0);
    }
}
