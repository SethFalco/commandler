package com.elypia.commandler.sending.senders;

import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.sending.IMessageSender;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.requests.restaction.MessageAction;

public class DefaultSender implements IMessageSender<Message> {

    @Override
    public MessageAction send(MessageEvent event, Message toSend) {
        return event.getMessageEvent().getChannel().sendMessage(toSend);
    }
}
