package com.elypia.commandler.sending;

import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.sending.senders.*;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.Message;

import java.util.*;
import java.util.function.Consumer;

public class Sender {

    private Map<Class<?>, IMessageSender> senders;

    public Sender() {
        senders = new HashMap<>();

        registerSender(Message.class, new DefaultSender());
        registerSender(EmbedBuilder.class, new EmbedBuilderSender());
        registerSender(MessageBuilder.class, new MessageBuilderSender());
        registerSender(String.class, new StringSender());
    }

    public <T> void registerSender(Class<T> t, IMessageSender<T> parser) {
        if (senders.put(t, parser) != null)
            System.err.printf("Replaced existing %s with the new sender.\n", t.getName());
    }

    public void sendAsMessage(MessageEvent event, Object object) {
        sendAsMessage(event, object, null);
    }

    public void sendAsMessage(MessageEvent event, Object object, Consumer<Message> success) {
        Class<?> clazz = object.getClass();
        boolean isArray = clazz.isArray();
        Class<?> component = isArray ? clazz.getComponentType() : clazz;
        boolean defaultString = false;
        IMessageSender sender = senders.get(component);
        Object[] array = isArray ? (Object[])object : new Object[] {object};

        if (sender == null) {
            sender = senders.get(String.class);
            defaultString = true;
        }

        for (Object o : array) {
            if (defaultString)
                sender.send(event, o.toString()).queue(success);
            else
                sender.send(event, o).queue(success);
        }
    }
}
