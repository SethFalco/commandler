package com.elypia.commandler.jda.parsing.parsers;

import com.elypia.commandler.data.SearchScope;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.jda.parsing.parsers.impl.JDAParser;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Role;

import java.util.Collection;

public class RoleParser extends JDAParser<Role> {

    public RoleParser(JDA jda) {
        super(jda);
    }

    @Override
    public Role parse(MessageEvent event, String input, SearchScope scope) throws IllegalArgumentException {
        Collection<Role> roles = jda.getRoles();

        for (Role role : roles) {
            if (role.getId().equals(input) || role.getAsMention().equals(input) || role.getName().equalsIgnoreCase(input))
                return role;
        }

        throw new IllegalArgumentException("Parameter `" + input + "` could not be be linked to a role.");
    }
}
