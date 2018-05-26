package com.elypia.commandlerbot;

import com.elypia.commandler.jda.events.MessageEvent;

import java.util.logging.Level;

import static java.util.logging.Level.*;


public class BotUtils {

    public static final long DEV = 127578559790186497L;

    public void log(Level level, MessageEvent event, String message) {
        String format = "```diff\n%s %s\n```";
        String color;

        if (level == FINE || level == FINER || level == FINEST)
            color = "+";

        else if (level == SEVERE || level == WARNING)
            color = "-";

        else
            color = "•";

        event.getMessageEvent().getJDA().getUserById(DEV).openPrivateChannel().queue(o -> {
            o.sendMessage(String.format(format, color, message)).queue();
        });
    }

    public <T extends Exception> void log(MessageEvent event, String message, T exception) {

    }
}
