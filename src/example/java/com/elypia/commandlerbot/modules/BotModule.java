package com.elypia.commandlerbot.modules;

import com.elypia.commandler.modules.CommandHandler;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.validation.command.*;
import com.elypia.commandler.events.MessageEvent;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.*;

import java.time.*;
import java.util.Collection;
import java.util.stream.Collectors;

@Module(name = "Bot Utilities", aliases = {"bot", "robot", "botto"}, description = "Obtain information on the bot itself or it's developers, or perform general bot functionality.")
public class BotModule extends CommandHandler {

    public static final String BOT_URL = "https://discordapp.com/oauth2/authorize?client_id=%s&scope=bot";

    private static final OffsetDateTime BOT_TIME = OffsetDateTime.of(2016, 7, 19, 1, 52, 0, 0, ZoneOffset.ofHours(0));

    @Static
    @Command(name = "Ping!", aliases = "ping", help = "Ping the bot to make sure it's still alive and responding!")
    public String ping() {
        return "pong!";
    }

    @Static
    @Command(name = "Pong!", aliases = "pong")
    public String pong() {
        return "ping!";
    }

    @Static
    @Command(name = "Say", aliases = "say", help = "Have Commandler say something after you, deleting the message if possible.")
    @Param(name = "message", help = "The message to repeat.")
    public String say(MessageEvent event, String message) {
        event.tryDeleteMessage();
        return message;
    }

    @Default
    @Command(name = "Bot Info", aliases = "info", help = "Get information on the bot and it's developers.")
    public EmbedBuilder info() {
        User user = jda.getSelfUser();
        EmbedBuilder builder = new EmbedBuilder();

        builder.setAuthor(user.getName());
        builder.addField("Developer", "Seth", true);
        builder.addField("Artist", "Téa", true);

        return builder;
    }

    @Scope(ChannelType.TEXT)
    @Command(name = "Bot Invites", aliases = "invites", help = "A list of invite links for all bots in the current guild.")
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
