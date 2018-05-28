package com.elypia.commandler.sending;

import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.sending.senders.DefaultSender;
import com.elypia.commandler.sending.senders.EmbedBuilderSender;
import com.elypia.commandler.sending.senders.MessageBuilderSender;
import com.elypia.commandler.sending.senders.StringSender;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.requests.restaction.MessageAction;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Sender {

    protected Map<Class<?>, MessageSender> senders;

    public Sender() {
        this(true);
    }

    public Sender(boolean auto) {
        senders = new HashMap<>();

        if (auto) {
            registerSender(Message.class, new DefaultSender());
            registerSender(EmbedBuilder.class, new EmbedBuilderSender());
            registerSender(MessageBuilder.class, new MessageBuilderSender());
            registerSender(String.class, new StringSender());
        }
    }

    public <T> void registerSender(Class<T> t, MessageSender<T> parser) {
        if (senders.keySet().contains(t))
            throw new IllegalArgumentException("Sender for this type of object has already been registered.");

        senders.put(t, parser);
    }

    public void sendAsMessage(MessageEvent event, Object object, Consumer<Message> success) throws IllegalArgumentException {
        Class<?> clazz = object.getClass();
        MessageSender sender;

        if (clazz.isArray()) {
            Class<?> type = clazz.getComponentType();
            sender = senders.get(type);

            if (sender == null)
                sender = senders.get(String.class);

            Object[] array = (Object[])object;

            for (int i = 0; i < array.length; i++)
                sender.send(event, type.cast(array[i])).queue(success);
        } else {
            sender = senders.get(clazz);
            sender.send(event, object).queue(success);
        }
    }
}
