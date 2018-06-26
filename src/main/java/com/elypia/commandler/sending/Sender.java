package com.elypia.commandler.sending;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.confiler.Confiler;
import com.elypia.commandler.confiler.reactions.ReactionRecord;
import com.elypia.commandler.events.*;
import com.elypia.commandler.metadata.AbstractMetaCommand;
import com.elypia.commandler.sending.senders.*;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.Message;

import java.util.*;
import java.util.function.Consumer;

public class Sender {

    private Commandler commandler;
    private Confiler confiler;

    private Map<Class<?>, IMessageSender> senders;

    public Sender(Commandler commandler) {
        this.commandler = Objects.requireNonNull(commandler);
        confiler = commandler.getConfiler();

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

    public void sendAsMessage(CommandEvent event, Object object) {
        sendAsMessage(event, object, null);
    }

    public void sendAsMessage(AbstractEvent event, Object object, Consumer<Message> success) {
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

        Consumer<Message> consumer = (message) -> {
            AbstractMetaCommand metaCommand = event.getMetaCommand();

            if (metaCommand.isReactionTracking()) {
                ReactionRecord record = new ReactionRecord();
                record.setCommandId(metaCommand.getCommand().id());
                record.setChannelId(message.getChannel().getIdLong());
                record.setMessageId(message.getIdLong());
                record.setOwnerId(event.getMessage().getAuthor().getIdLong());
                record.setParams(event.getParams());

                confiler.getReactionController().startTrackingEvents(record);
            }

            if (metaCommand.getMethod().isAnnotationPresent(Emoji.class)) {
                Emoji[] params = metaCommand.getMethod().getAnnotationsByType(Emoji.class);

                for (Emoji e : params)
                    message.addReaction(e.alias()).queue();
            }

            if (success != null)
                success.accept(message);
        };

        for (Object o : array) {
            if (defaultString)
                sender.send(event, o.toString()).queue(consumer);
            else
                sender.send(event, o).queue(consumer);
        }
    }
}
