package com.elypia.commandler.parsing.parsers.jda;

import com.elypia.commandler.data.SearchScope;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.parsing.IParamParser;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class UserParser implements IParamParser<User> {

    @Override
    public User parse(MessageEvent event, SearchScope scope, String input) throws IllegalArgumentException {
        final Collection<User> users = new ArrayList<>();

        switch (scope) {
            case GLOBAL:
                users.addAll(event.getMessageEvent().getJDA().getUsers());
                break;

            case MUTUAL:
                User user = event.getMessageEvent().getAuthor();
                Collection<Guild> guilds = user.getMutualGuilds();
                guilds.forEach(g -> users.addAll(g.getMembers().stream().map(Member::getUser).collect(Collectors.toList())));
                break;

            case LOCAL:
                users.addAll(event.getMessageEvent().getGuild().getMembers().stream().map(Member::getUser).collect(Collectors.toList()));
                break;
        }

        for (User user : users) {
            if (user.getId().equals(input) || user.getName().equalsIgnoreCase(input))
                return user;
        }

        throw new IllegalArgumentException("Parameter `" + input + "` could not be be linked to a channel.");
    }
}
