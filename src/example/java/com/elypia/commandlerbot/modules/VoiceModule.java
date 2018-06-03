package com.elypia.commandlerbot.modules;

import com.elypia.commandler.CommandHandler;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.filter.Search;
import com.elypia.commandler.annotations.validation.command.Scope;
import com.elypia.commandler.events.MessageEvent;
import net.dv8tion.jda.core.entities.*;

import java.util.*;
import java.util.stream.Collectors;

import static com.elypia.commandler.data.SearchScope.LOCAL;
import static net.dv8tion.jda.core.entities.ChannelType.TEXT;

@Module(name = "Guild Voice Channel", aliases = {"voice", "vc"}, description = "Convenience commands for voice channels.")
public class VoiceModule extends CommandHandler {

    @Scope(TEXT)
    @Command(aliases = {"mention", "at"}, help = "Mention all the users in a voice channel.")
    @Param(name = "channel", help = "The channel(s) to mention users from.")
    public String mention(MessageEvent event, @Search(LOCAL) VoiceChannel[] channels) {
        Set<Member> members = new HashSet<>();
        Arrays.stream(channels).map(VoiceChannel::getMembers).forEach(members::addAll);
        Set<User> users = members.stream().map(Member::getUser).filter(o -> !o.isBot()).collect(Collectors.toSet());

         if (users.remove(event.getMessageEvent().getAuthor())) {
             if (users.size() == 0) {
                 String there = channels.length == 1 ? "there" : "those";
                 return String.format("But... you're the only user in %s? ^-^'", there);
             }
         }

        StringJoiner joiner = new StringJoiner(" | ");
        users.forEach(o -> joiner.add(o.getAsMention()));

        return joiner.length() == 0 ? "Wait... who should I be mentioning?" : joiner.toString();
    }
}
