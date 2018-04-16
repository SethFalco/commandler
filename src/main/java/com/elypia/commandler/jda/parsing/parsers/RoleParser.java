package com.elypia.commandler.jda.parsing.parsers;

import com.elypia.commandler.parsing.impl.Parser;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Role;

import java.util.Collection;

public class RoleParser implements Parser<Role> {

    private JDA jda;

    public RoleParser(JDA jda) {
        this.jda = jda;
    }

    @Override
    public Role parse(String input) throws IllegalArgumentException {
        Collection<Role> roles = jda.getRoles();

        for (Role role : roles) {
            if (role.getId().equals(input) || role.getAsMention().equals(input) || role.getName().equalsIgnoreCase(input))
                return role;
        }

        throw new IllegalArgumentException("Parameter `" + input + "` could not be be linked to a role.");
    }
}
