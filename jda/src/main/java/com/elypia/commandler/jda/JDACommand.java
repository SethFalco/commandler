package com.elypia.commandler.jda;

import com.elypia.commandler.CommandEvent;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.*;
import net.dv8tion.jda.core.requests.RequestFuture;

import java.util.concurrent.ExecutionException;

public class JDACommand extends CommandEvent<JDA, GenericMessageEvent, Message> {

    private Message message;
    private RequestFuture<Message> future;

    public JDACommand(CommandEvent<JDA, GenericMessageEvent, Message> command) {
        super(command.getCommandler(), command.getInput(), command.getSource());

        GenericMessageEvent source = command.getSource();

        if (source instanceof MessageReceivedEvent)
            message = ((MessageReceivedEvent)source).getMessage();
        else if (source instanceof MessageUpdateEvent)
            message = ((MessageUpdateEvent)source).getMessage();
        else
            future = source.getChannel().getMessageById(source.getMessageId()).submit();
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

    public Message getMessage() {
        if (message == null) {
            try {
                message = future.get();
            } catch (ExecutionException | InterruptedException e) {
                // ? We don't make a way for users to cancel this.
                e.printStackTrace();
            }
        }

        return message;
    }
}
