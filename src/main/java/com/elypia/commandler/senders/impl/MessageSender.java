package com.elypia.commandler.senders.impl;

import com.elypia.commandler.events.MessageEvent;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.requests.restaction.MessageAction;

import java.util.function.Consumer;

public abstract class MessageSender<T> {

    protected JDA jda;

    public MessageSender(JDA jda) {
        this.jda = jda;
    }

    public abstract MessageAction send(MessageEvent event, T toSend);

    public JDA getJDA() {
            return jda;
        }
}
