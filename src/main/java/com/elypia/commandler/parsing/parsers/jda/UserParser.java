package com.elypia.commandler.parsing.parsers.jda;

import com.elypia.commandler.data.SearchScope;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.parsing.impl.JDAParser;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class UserParser extends JDAParser<User> {

    public UserParser(JDA jda) {
        super(jda);
    }

    @Override
    public User parse(MessageEvent event, SearchScope scope, String input) throws IllegalArgumentException {
        final Collection<User> users = new ArrayList<>();

        switch (scope) {
            case GLOBAL:
                users.addAll(jda.getUsers());
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
