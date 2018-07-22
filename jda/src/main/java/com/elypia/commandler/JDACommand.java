package com.elypia.commandler;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;

public class JDACommand extends CommandEvent<JDA, GenericMessageEvent, Message> {

    public JDACommand(CommandEvent<JDA, GenericMessageEvent, Message> command) {
        super(command.commandler, command.input, command.event);
    }

    @Override
    public Message reply(Object output) {
        Message message = super.reply(output);
        event.getChannel().sendMessage(message).queue();
        return message;
    }
}
