package com.elypia.commandler.jda.reactions;

import com.elypia.commandler.*;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;

import java.util.function.Consumer;

public class ReactionEvent extends CommandEvent {

    private MessageReactionAddEvent event;
    private ReactionRecord record;

    public ReactionEvent(Commandler commandler, MessageReactionAddEvent event, ReactionRecord record) {
        super(commandler, null, event);

        this.event = event;
        this.record = record;
    }

    public void setMessage(Message message) {
        getMessage(msg -> msg.editMessage(message).queue());
    }

    public void appendMessage(String message) {
        getMessage(msg -> msg.editMessage(msg.getContentRaw() + "\n" + message).queue());
    }

    @Override
    public Object reply(Object output) {
        return null;
    }

    public void deleteMessage() {
        event.getChannel().deleteMessageById(record.getMessageId()).queue();
    }

    public void deleteParentMessage() {
        record.getParentMessage().delete().queue();
    }

    public void getMessage(Consumer<Message> consumer) {
        event.getChannel().getMessageById(record.getMessageId()).queue(o -> {
            message = o;
            consumer.accept(o);
        });
    }

    public MessageReactionAddEvent getReactionAddEvent() {
        return event;
    }

    public com.elypia.commandler.reactions.ReactionRecord getReactionRecord() {
        return record;
    }
}
