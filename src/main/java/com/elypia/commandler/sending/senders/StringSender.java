package com.elypia.commandler.sending.senders;

import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.sending.MessageSender;
import net.dv8tion.jda.core.requests.restaction.MessageAction;

public class StringSender implements MessageSender<String> {

    @Override
    public MessageAction send(MessageEvent event, String toSend) {
        return event.getMessageEvent().getChannel().sendMessage(toSend);
    }
}
