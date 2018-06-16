package com.elypia.commandlerbot.modules;

import com.elypia.commandler.modules.CommandHandler;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.filter.Search;
import com.elypia.commandler.annotations.validation.command.Scope;
import com.elypia.commandler.events.MessageEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;

import java.util.List;

import static com.elypia.commandler.data.SearchScope.*;

@Module(name = "Guild Emoticons", aliases = {"emote", "emoji", "emoticon", "emotes"}, description = "Interact with guild custom emotes, even in other guilds.")
public class EmotesModule extends CommandHandler {

    @Scope(ChannelType.TEXT)
    @CommandGroup("list")
    @Command(name = "List Emotes", aliases = "list", help = "List all of the custom emotes in the guild.")
    public String listEmotes(MessageEvent event) {
        return listEmotes(event.getMessageEvent().getGuild());
    }

    @Scope(ChannelType.PRIVATE)
    @CommandGroup("list")
    @Param(name = "guild", help = "The guild to obtain emotes from.")
    public String listEmotes(@Search(MUTUAL) Guild guild) {
        List<Emote> emotes = guild.getEmotes();
        int count = emotes.size();

        if (count == 0)
            return guild.getName() + " don't actually have any emotes though... ^-^'";

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
    @Command(name = "Get Emote", aliases = {"get", "post"}, help = "Post an emote in the chat!")
    @Param(name = "emote", help = "Name or ID of the emote you want to post.")
    public EmbedBuilder post(@Search(GLOBAL) Emote emote) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setImage(emote.getImageUrl());
        return builder;
    }
}
