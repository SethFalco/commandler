package com.elypia.commandler.sending.senders;

import com.elypia.commandler.events.AbstractEvent;
import com.elypia.commandler.sending.IMessageSender;
import net.dv8tion.jda.core.requests.restaction.MessageAction;

public class StringSender implements IMessageSender<String> {

    @Override
    public MessageAction send(AbstractEvent event, String toSend) {
        return event.getMessageEvent().getChannel().sendMessage(toSend);
    }
}
