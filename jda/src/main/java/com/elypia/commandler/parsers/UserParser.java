package com.elypia.commandler.parsers;

import com.elypia.commandler.*;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;

import java.util.*;

public class UserParser implements IJDAParser<User> {

    @Override
    public User parse(JDACommand event, Class<? extends User> type, String input) {
        Set<User> users = new HashSet<>(event.getClient().getUsers());
        List<User> matches = new ArrayList<>();

        users.forEach(user -> {
            if (user.getId().equals(input))
                matches.add(user);

            else if (user.getName().equalsIgnoreCase(input))
                matches.add(user);

            else if (user.getAsMention().equals(input))
                matches.add(user);

            else if (user.toString().equalsIgnoreCase(input))
                matches.add(user);

            else if (input.equals("<@!" + user.getId() + ">"))
                matches.add(user);
        });

        return matches.isEmpty() ? null : matches.get(0);
    }
}
