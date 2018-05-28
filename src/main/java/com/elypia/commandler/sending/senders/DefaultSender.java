package com.elypia.commandler.sending.senders;

import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.sending.MessageSender;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.requests.restaction.MessageAction;

public class DefaultSender implements MessageSender<Message> {

    @Override
    public MessageAction send(MessageEvent event, Message toSend) {
        return event.getMessageEvent().getChannel().sendMessage(toSend);
    }
}
