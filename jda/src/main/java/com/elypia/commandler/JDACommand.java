package com.elypia.commandler;

import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.*;
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

    public boolean deleteMessage() {
        if (!event.isFromType(ChannelType.TEXT))
            return false;

        Member member = event.getGuild().getSelfMember();

        if (member.hasPermission(event.getTextChannel(), Permission.MESSAGE_MANAGE)) {
            getMessage().delete().queue();
            return true;
        }

        return false;
    }

    @Override
    public Message getMessage() {
        return null;
    }
}
