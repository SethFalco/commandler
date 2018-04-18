package com.elypia.commandlerbot.modules;

import com.elypia.commandler.CommandHandler;
import com.elypia.commandler.annotations.command.*;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.User;

@Module(aliases = "bot", help = "Fundamental bot debugging or information commands.")
public class BotModule extends CommandHandler {

    @Static
    @Command(aliases = "ping", help = "pong")
    public String ping() {
        return "pong!";
    }

    @Command(aliases = "info", help = "Get information on the bot and it's development.")
    public EmbedBuilder info() {
        User user = jda.getSelfUser();
        EmbedBuilder builder = new EmbedBuilder();

        builder.setAuthor(user.getName());
        builder.addField("Developer", "Seth", true);
        builder.addField("Artist", "Téa", true);

        return builder;
    }
}
