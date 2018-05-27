package com.elypia.commandlerbot.modules;

import com.elypia.commandler.CommandHandler;
import com.elypia.commandler.annotations.Command;
import com.elypia.commandler.annotations.CommandGroup;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.Param;
import com.elypia.commandler.annotations.access.Permissions;
import com.elypia.commandler.annotations.access.Scope;
import com.elypia.commandler.annotations.filter.Search;
import com.elypia.commandler.events.MessageEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;

import static com.elypia.commandler.data.SearchScope.MUTUAL;
import static net.dv8tion.jda.core.entities.ChannelType.PRIVATE;
import static net.dv8tion.jda.core.entities.ChannelType.TEXT;

@Module(
    name = "Guild",
    aliases = {"guild"},
    description = "Commands that retrieve information or perform them to a specified guild."
)
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

    @Permissions(Permission.MANAGE_SERVER)
    @Scope(ChannelType.TEXT)
    @CommandGroup("prune")
    @Command(aliases = "prune", help = "Delete messages in the guild in bulk.")
    @Param(name = "count", help = "The number of messages to delete.")
    public void prune(MessageEvent event, int count) {
        prune(event, count, event.getMessageEvent().getTextChannel());
    }

    @Permissions(Permission.MANAGE_SERVER)
    @Scope(ChannelType.TEXT)
    @CommandGroup("prune")
    @Param(name = "count", help = "The number of messages to delete.")
    @Param(name = "channel", help = "The channel to delete messages in.")
    public void prune(MessageEvent event, int count, TextChannel channel) {
        if (count < 2 || count > 100) {
            event.reply("Please specify between 2 and 100 messages.");
            return;
        }

        channel.getHistoryBefore(event.getMessageEvent().getMessage(), count).queue(o -> {
            channel.deleteMessages(o.getRetrievedHistory()).queue(ob -> {
                event.tryDeleteMessage();
            });
        });
    }
}
