package com.elypia.commandlerbot.modules;

import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.filter.Search;
import com.elypia.commandler.annotations.validation.command.Scope;
import com.elypia.commandler.data.SearchScope;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.modules.CommandHandler;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;

@Scope(ChannelType.TEXT)
@Module(name = "Guild", aliases = {"guild", "server"}, description = "Commands that retrieve information or perform them to a specified guild.")
public class GuildModule extends CommandHandler {

    @Default
    @CommandGroup("info")
    @Command(name = "Guild Info", aliases = {"info", "information", "wiki"}, help = "Obtain information on the guild.")
    public EmbedBuilder info(MessageEvent event) {
        return info(event.getMessageEvent().getGuild());
    }

    @Scope(ChannelType.PRIVATE)
    @CommandGroup("info")
    @Param(name = "guild", help = "The guild to obtain information for.")
    public EmbedBuilder info(@Search(SearchScope.MUTUAL) Guild guild) {
        EmbedBuilder builder = new EmbedBuilder();

        builder.setAuthor(guild.getName());
        builder.addField("Owner", guild.getOwner().getEffectiveName(), true);
        builder.addField("Total Members", String.valueOf(guild.getMembers().size()), true);

        return builder;
    }
}
