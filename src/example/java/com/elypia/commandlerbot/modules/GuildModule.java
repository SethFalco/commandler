package com.elypia.commandlerbot.modules;

import com.elypia.commandler.modules.CommandHandler;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.filter.Search;
import com.elypia.commandler.annotations.validation.command.Scope;
import com.elypia.commandler.events.MessageEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;

import static com.elypia.commandler.data.SearchScope.MUTUAL;

@Scope(ChannelType.TEXT)
@Module(name = "Guild", aliases = {"guild"}, description = "Commands that retrieve information or perform them to a specified guild.")
public class GuildModule extends CommandHandler {

    @CommandGroup("info")
    @Command(name = "Guild Info", aliases = "info", help = "Get information on a guild.")
    public EmbedBuilder info(MessageEvent event) {
        return info(event.getMessageEvent().getGuild());
    }

    @Scope(ChannelType.PRIVATE)
    @CommandGroup("info")
    @Param(name = "guild", help = "The guild to display information for.")
    public EmbedBuilder info(@Search(MUTUAL) Guild guild) {
        EmbedBuilder builder = new EmbedBuilder();

        builder.setAuthor(guild.getName());
        builder.addField("Owner", guild.getOwner().getEffectiveName(), true);
        builder.addField("Total Members", String.valueOf(guild.getMembers().size()), true);

        return builder;
    }
}
