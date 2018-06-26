package com.elypia.commandler.sending;

import com.elypia.commandler.events.AbstractEvent;
import net.dv8tion.jda.core.requests.restaction.MessageAction;

public interface IMessageSender<T> {
    MessageAction send(AbstractEvent event, T toSend);
}
