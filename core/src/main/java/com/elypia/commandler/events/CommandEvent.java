package com.elypia.commandler.events;

import com.elypia.commandler.*;
import com.elypia.commandler.impl.Confiler;
import com.elypia.commandler.confiler.reactions.ReactionRecord;
import com.elypia.commandler.metadata.AbstractMetaCommand;
import com.elypia.commandler.building.Builder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;

import java.util.*;
import java.util.function.Consumer;

public class CommandEvent<E, M> {

    protected E event;

    /**
     * The parent {@link Commandler} instance that spawned this {@link CommandEvent}.
     */

    protected Commandler commandler;

    /**
     * A reference to the configuration object for our {@link #confiler}.
     */

    protected Confiler confiler;

    /**
     * A list of all parameters associated with this commands. This list may contain
     * both {@link String}s and {@link String[]}s. List parameters (comma seperated) are stored
     * as an array.
     */

    protected CommandInput input;

    private Map<String, Object> objectStore;

    /**
     * The command associated with this command if the command is valid.
     */

    protected AbstractMetaCommand metaCommand;

    public CommandEvent(Commandler commandler, E event, M) {
        this.commandler = commandler;
        this.event = event;
        this.message = message;
        reactions = new ArrayList<>();
        objectStore = new HashMap<>();

        confiler = commandler.getConfiler();
    }

    public void storeObject(String name, Object object) {
        objectStore.put(name, object);
    }

    /**
     * Send a response to the {@link MessageChannel} this event occured in. <br>
     * This is useful when using {@link Consumer}s as you're unable to return from within
     * an anonymous function.
     *
     * @param object The item to send to the {@link Builder} to process.
     * @param <T> Type of object to send, this type is compared to the types in our
     * {@link Builder} so we know how to send it.
     */

    public <T> void reply(T object) {
        Builder builder = commandler.getDispatcher().getBuilder();
        Message message = builder.buildMessage(this, object);
        event.getChannel().sendMessage(message).queue(msg -> {
            if (!reactions.isEmpty()) {
                ReactionRecord record = new ReactionRecord();
                record.setCommandId(metaCommand.getCommand().id());
                record.setChannelId(msg.getChannel().getIdLong());
                record.setMessageId(msg.getIdLong());
                record.setParentMessage(this.message);
                record.setOwnerId(msg.getAuthor().getIdLong());

                Map<String, List<String>> params = new HashMap<>();
                for (int i = 0; i < params.size(); i++)
                    params.put(metaCommand.getInputParams().get(i).getParamAnnotation().name(), this.params.get(i));

                record.setParams(params);

                record.setObjectStore(objectStore);
                confiler.getReactionController().startTrackingEvents(record);
            }

            reactions.forEach(s -> msg.addReaction(s).queue());
        });
    }

    /**
     * If the bot has permission to, delete the message that triggered this event. <br>
     * This will do nothing in {@link ChannelType#PRIVATE} channels a user may only delete it's
     * own messages and the bot can't trigger it's own event.
     *
     * @return If the bot sent a request to delete the {@link Message}.
     */

    public boolean tryDeleteMessage() {
        if (!event.isFromType(ChannelType.TEXT))
            return false;

        if (event.getGuild().getSelfMember().hasPermission(event.getTextChannel(), Permission.MESSAGE_MANAGE)) {
            getMessage().delete().queue();
            return true;
        }

        return false;
    }

    /**
     * Trigger another commands which branches from this commands. The new commands
     * will use the same {@link GenericMessageEvent} and {@link Message} but with the commands
     * specified instead. <br>
     * <strong>Do not</strong> include the prefix when executing a trigger. <br>
     * Example: <br>
     * <em>event.trigger("bot help");</em>
     *
     * @param trigger The new commands to process instead.
     */

    public void trigger(String trigger) {
        String command = event.getJDA().getSelfUser().getAsMention();
        command += trigger;

        if (this instanceof ReactionEvent)
            commandler.getDispatcher().processCommand(event, message, command);
        else
            commandler.getDispatcher().processCommand(event, message, command);

    }

    /**
     * @return The {@link GenericMessageEvent} that caused this event.
     */

    public GenericMessageEvent getMessageEvent() {
        return event;
    }

    /**
     * @return The {@link Message} that caused this event.
     */

    public M getMessage() {
        return message;
    }

    public Commandler getCommandler() {
        return commandler;
    }

    public Confiler getConfiler() {
        return confiler;
    }

    public AbstractMetaCommand getMetaCommand() {
        return metaCommand;
    }

    public void setMetaCommand(AbstractMetaCommand metaCommand) {
        this.metaCommand = metaCommand;
    }
}
