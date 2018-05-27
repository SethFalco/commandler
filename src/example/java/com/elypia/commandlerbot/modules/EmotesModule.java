package com.elypia.commandlerbot.modules;

import com.elypia.commandler.CommandHandler;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.access.Scope;
import com.elypia.commandler.annotations.filter.Search;
import com.elypia.commandler.events.MessageEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Guild;

import java.util.List;

import static com.elypia.commandler.data.SearchScope.GLOBAL;
import static com.elypia.commandler.data.SearchScope.MUTUAL;

@Module(
    name = "Guild Emoticons",
    aliases = {"emote", "emoji", "emoticon"},
    description = "Display a list of guild emoticons or obtain images."
)
public class EmotesModule extends CommandHandler {

    @Scope(ChannelType.TEXT)
    @CommandGroup("list")
    @Command(aliases = "list", help = "List all of the custom emotes in this guild.")
    public String listEmotes(MessageEvent event) {
        return listEmotes(event.getMessageEvent().getGuild());
    }

    @Scope(ChannelType.PRIVATE)
    @CommandGroup("list")
    @Param(name = "guild", help = "The guild to get emotes from.")
    public String listEmotes(@Search(MUTUAL) Guild guild) {
        List<Emote> emotes = guild.getEmotes();
        int count = emotes.size();

        if (count == 0)
            return "You don't actually have any emotes though... ^-^'";

        int length = (int)(Math.sqrt(count) + 1) * 2;

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            if (i % length == 0)
                builder.append("\n");

            builder.append(emotes.get(i).getAsMention());
        }

        return builder.toString();
    }

    @Default
    @CommandGroup("post")
    @Command(aliases = {"get", "post"}, help = "Post an emote in the chat!")
    @Param(name = "emote", help = "A way to identify the emote you want to post.")
    public EmbedBuilder post(@Search(GLOBAL) Emote emote) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setImage(emote.getImageUrl());
        return builder;
    }
}
