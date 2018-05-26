package com.elypia.commandlerbot.modules;

import com.elypia.commandler.annotations.Command;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.Param;
import com.elypia.commandler.jda.JDACommandHandler;
import com.elypia.commandler.jda.annotations.access.Scope;
import com.elypia.commandler.jda.annotations.filter.Search;
import com.elypia.commandler.jda.events.MessageEvent;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.VoiceChannel;

import java.util.*;

import static com.elypia.commandler.jda.data.SearchScope.LOCAL;
import static net.dv8tion.jda.core.entities.ChannelType.TEXT;

@Module(
    name = "Guild Voice Channel",
    aliases = {"voice", "vc"},
    description = "Convenience commands for voice channels."
)
public class VoiceModule extends JDACommandHandler {

    @Scope(TEXT)
    @Command(aliases = {"mention", "at"}, help = "Mention all the users in a voice channel.")
    @Param(name = "channel", help = "The channel(s) to mention users from.")
    public String mention(MessageEvent event, @Search(LOCAL) VoiceChannel[] channels) {
        Set<Member> members = new HashSet<>();
        StringJoiner joiner = new StringJoiner(" | ");
        Arrays.stream(channels).filter(Objects::nonNull).forEach(o -> members.addAll(o.getMembers()));

        for (Member member : members) {
            if (!member.getUser().isBot() && member != event.getMessageEvent().getMember())
                joiner.add(member.getAsMention());
        }

        return joiner.length() == 0 ? "Wait... who should I be mentioning?" : joiner.toString();
    }
}
