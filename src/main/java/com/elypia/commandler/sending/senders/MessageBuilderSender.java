package com.elypia.commandler.sending.senders;

import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.sending.MessageSender;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.requests.restaction.MessageAction;

public class MessageBuilderSender implements MessageSender<MessageBuilder> {

    @Override
    public MessageAction send(MessageEvent event, MessageBuilder toSend) {
        return event.getMessageEvent().getChannel().sendMessage(toSend.build());
    }
}
