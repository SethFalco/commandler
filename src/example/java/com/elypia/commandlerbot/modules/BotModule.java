package com.elypia.commandlerbot.modules;

import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.validation.param.Everyone;
import com.elypia.commandler.confiler.reactions.IReactionController;
import com.elypia.commandler.events.*;
import com.elypia.commandler.exceptions.MalformedCommandException;
import com.elypia.commandler.modules.CommandHandler;
import net.dv8tion.jda.core.EmbedBuilder;

@Module(name = "Bot", aliases = {"bot", "robot"}, description = "Get info on the bot itself and it's development!")
public class BotModule extends CommandHandler {

    /**
     * This is the {@link Default} command for the module, and can be executed
     * in two ways: <br>
     * <strong>!bot info</strong> and <strong>!bot</strong> <br>
     * Each module can only have up to one default command.
     */

    @Default
    @Command(name = "Bot Info", aliases = "info", help = "Get information on the bot and it's developers.")
    public EmbedBuilder info() {
        EmbedBuilder builder = new EmbedBuilder();
        builder.addField("Developer", "Seth", true);
        builder.addField("Artist", "Téa", true);

        return builder;
    }

    /**
     * This is a static command, this means the command can be executed without
     * specifying the module name. In this case in can be performed in two ways: <br>
     * <strong>!bot ping</strong> and <strong>!ping</strong>
     * Modules can have many static commands, however you can not have multiple
     * static commands with the same alias.
     */

    @Static
    @Command(name = "Ping!", aliases = "ping", help = "Ping the bot to make sure it's still alive and responding!")
    public String ping() {
        return "pong!";
    }

    /**
     * This command is hidden from help messages as no {@link Command#help()} is specified. <br>
     * Other than this, it can be used like any other commands, it's merely hidden.
     */

    @Static
    @Command(name = "Pong!", aliases = "pong")
    public String pong() {
        return "ping!";
    }

    /**
     * This command takes parameters, parameters are parsed internally by
     * Commandler so there is no need to pass Object or String arrays around. <br>
     * Any parameters of the method become parameters the user must specify to perform the command
     * with the exception of anything which is an instance of {@link AbstractEvent}. <br>
     * For example, in this case, this command takes one parameter from the user which is
     * <code>String message</code>. <br>
     * The {@link CommandEvent} parameter is an instance of {@link AbstractEvent} and is optional,
     * but useful if you need access to the origin event data. <br>
     * <br>
     * The {@link Everyone} annotation is used as in line validation, Everyone is a provided
     * annotation which will ensure if the user doesn't have the everyone permission, they aren't
     * using @everyone or @here in the parameter. <br>
     * (Else in this case they could bypass their lack of permission by making the bot say it for them.) <br>
     *
     * Specifying an {@link Param} annotation for input parameters is required else you'll get a
     * {@link MalformedCommandException} and Commandler won't register your module, this is used to build
     * the help commands and website.
     */

    @Static
    @TrackReactions
    @Command(id = 3, name = "Say", aliases = "say", help = "I'll say what you want; deleting your message if possible.")
    @Param(name = "message", help = "The message to repeat.")
    @Emoji(alias = "\uD83D\uDD01", help = "Make the bot create a new line and append the same text.")
    public String say(CommandEvent event, @Everyone String message) {
        event.tryDeleteMessage();
        return message;
    }

    /**
     * Here we introduce reaction handling, in the above command we specify an ID and
     * say that it is ReactionTracking with the {@link TrackReactions} annotation.
     * This means an event will be raised to the registered {@link IReactionController}
     * with the message ID of any resulting messages from this method either through the
     * return or the {@link AbstractEvent#reply(Object)} method.
     */

    @Reaction(id = 3, emotes = "\uD83D\uDD01")
    public void sayReaction(ReactionEvent event) {
        event.getMessage(o -> {
            String newMessage = o.getContentRaw() + "\n" + o.getContentRaw();
            o.editMessage(newMessage).queue();
        });
    }
}
