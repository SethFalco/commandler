package com.elypia.commandlerbot.modules;

import com.elypia.commandler.CommandHandler;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.jda.annotations.filter.Search;
import com.elypia.commandler.jda.events.MessageEvent;
import com.elypia.commandler.jda.annotations.access.Scope;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;

import static com.elypia.commandler.jda.data.SearchScope.MUTUAL;
import static net.dv8tion.jda.core.entities.ChannelType.*;

@Module(aliases = "guild", help = "Commands to get or perform functionality on a guild.")
public class GuildModule extends CommandHandler {

    @Scope(TEXT)
    @CommandGroup("info")
    @Command(aliases = "info", help = "Get information on a guild.")
    public EmbedBuilder info(MessageEvent event) {
        return info(event.getMessageEvent().getGuild());
    }

    @Scope(PRIVATE)
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
