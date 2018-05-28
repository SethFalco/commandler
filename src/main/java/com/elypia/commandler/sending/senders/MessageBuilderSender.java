package com.elypia.commandler.sending.senders;

import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.sending.IMessageSender;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.requests.restaction.MessageAction;

public class MessageBuilderSender implements IMessageSender<MessageBuilder> {

    @Override
    public MessageAction send(MessageEvent event, MessageBuilder toSend) {
        return event.getMessageEvent().getChannel().sendMessage(toSend.build());
    }
}
