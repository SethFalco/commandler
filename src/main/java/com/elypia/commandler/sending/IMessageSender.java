package com.elypia.commandler.sending;

import com.elypia.commandler.events.MessageEvent;
import net.dv8tion.jda.core.requests.restaction.MessageAction;

public interface IMessageSender<T> {
    MessageAction send(MessageEvent event, T toSend);
}
