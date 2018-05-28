package com.elypia.commandlerbot.modules;

import com.elypia.commandler.CommandHandler;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.access.Scope;
import com.elypia.commandler.events.MessageEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.stream.Collectors;

@Module(
    name = "Bot Commands and Utilities",
    aliases = {"bot", "robot"},
    description = "Fundamental commands for the bot itself, primiarly for debugging or to obtain public information."
)
public class BotModule extends CommandHandler {

    public static final String BOT_URL = "https://discordapp.com/oauth2/authorize?client_id=%s&scope=bot";

    private static final OffsetDateTime BOT_TIME = OffsetDateTime.of(2016, 7, 19, 1, 52, 0, 0, ZoneOffset.ofHours(0));

    @Static
    @Command(aliases = "ping", help = "pong")
    public String ping() {
        return "pong!";
    }

    @Static
    @Command(aliases = "pong")
    @Reaction(alias = "\uD83D\uDD01", help = "Want to keep ponging?")
    public String pong() {
        return "ping!";
    }

    @ReactionCommand(reaction = "\uD83D\uDD01", event = "pong")
    public String repeatPong() {
        return pong();
    }

    @Command(aliases = "say", help = "Repeat after me...")
    @Param(name = "message", help = "The message to repeat.")
    public String say(String message) {
        return message;
    }

    @Default
    @Command(aliases = "info", help = "Get information on the bot and it's development.")
    public EmbedBuilder info() {
        User user = jda.getSelfUser();
        EmbedBuilder builder = new EmbedBuilder();

        builder.setAuthor(user.getName());
        builder.addField("Developer", "Seth", true);
        builder.addField("Artist", "Téa", true);

        return builder;
    }

    @Scope(ChannelType.TEXT)
    @Command(aliases = "invites", help = "A list of invite links for all bots in the current guild.")
    public EmbedBuilder invites(MessageEvent event) {
        Guild guild = event.getMessageEvent().getGuild();
        Collection<Member> bots = guild.getMembers();

        Collection<User> users = bots.stream().map(Member::getUser).filter(User::isBot).filter(o -> {
            return o.getCreationTime().isAfter(BOT_TIME);
        }).collect(Collectors.toList());

        EmbedBuilder builder = new EmbedBuilder();
        users.forEach(o -> builder.addField(o.getName(), "[Invite link!](" + String.format(BOT_URL, o.getIdLong()) + ")", true));

        return builder;
    }
}
