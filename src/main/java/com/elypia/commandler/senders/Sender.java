package com.elypia.commandler.senders;

import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.senders.impl.MessageSender;
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

    protected JDA jda;
    protected Map<Class<?>, MessageSender> senders;

    public Sender(JDA jda) {
        this.jda = jda;
        senders = new HashMap<>();

        registerSender(Message.class, new MessageSender<Message>(jda) {

            @Override
            public MessageAction send(MessageEvent event, Message toSend) {
                return event.getMessageEvent().getChannel().sendMessage(toSend);
            }
        });

        registerSender(String.class, new MessageSender<String>(jda) {

            @Override
            public MessageAction send(MessageEvent event, String toSend) {
                return event.getMessageEvent().getChannel().sendMessage(toSend);
            }
        });

        registerSender(MessageBuilder.class, new MessageSender<MessageBuilder>(jda) {

            @Override
            public MessageAction send(MessageEvent event, MessageBuilder toSend) {
                return event.getMessageEvent().getChannel().sendMessage(toSend.build());
            }
        });

        registerSender(EmbedBuilder.class, new MessageSender<EmbedBuilder>(jda) {

            @Override
            public MessageAction send(MessageEvent event, EmbedBuilder toSend) {
                Guild guild = event.getMessageEvent().getGuild();

                if (guild != null) {
                    Color color = guild.getSelfMember().getColor();
                    toSend.setColor(color);
                }

                return event.getMessageEvent().getChannel().sendMessage(toSend.build());
            }
        });
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
