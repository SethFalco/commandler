package com.elypia.commandler.events;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.confiler.reactions.ReactionRecord;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;

import java.util.function.Consumer;

public class ReactionEvent extends AbstractEvent {

    private MessageReactionAddEvent event;
    private ReactionRecord record;

    public ReactionEvent(Commandler commandler, MessageReactionAddEvent event, ReactionRecord record) {
        super(commandler, event);

        this.event = event;
        this.record = record;
    }

    public MessageReactionAddEvent getReactionAddEvent() {
        return event;
    }

    public ReactionRecord getReactionRecord() {
        return record;
    }

    public void getMessage(Consumer<Message> consumer) {
        if (message == null) {
            event.getChannel().getMessageById(event.getMessageIdLong()).queue(o -> {
                message = o;
                consumer.accept(o);
            });

            return;
        }

        consumer.accept(message);
    }

    @Override
    public Message getMessage() {
        if (message == null)
            message = event.getChannel().getMessageById(event.getMessageIdLong()).complete();

        return message;
    }
}
