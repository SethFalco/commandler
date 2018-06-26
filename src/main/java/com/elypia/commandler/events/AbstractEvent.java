package com.elypia.commandler.events;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.confiler.Confiler;
import com.elypia.commandler.metadata.AbstractMetaCommand;
import com.elypia.commandler.sending.Sender;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;

import java.util.*;
import java.util.function.Consumer;

public abstract class AbstractEvent {

    protected GenericMessageEvent event;

    /**
     * The {@link Message} that triggered this commands. <br>
     * The contents of this message it not necessarily the commands the message event is parsing.
     *
     * @see #trigger(String)
     */

    protected Message message;

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

    protected List<List<String>> params;

    private Set<String> reactions;

    /**
     * The command associated with this command if the command is valid.
     */

    protected AbstractMetaCommand metaCommand;

    public AbstractEvent(Commandler commandler, GenericMessageEvent event) {
        this(commandler, event, null);
    }

    public AbstractEvent(Commandler commandler, GenericMessageEvent event, Message message) {
        this.commandler = commandler;
        this.event = event;
        this.message = message;
        reactions = new HashSet<>();

        confiler = commandler.getConfiler();
    }

    public void addReaction(String... unicode) {
        reactions.addAll(Arrays.asList(unicode));
    }


    public Set<String> getReactions() {
        return reactions;
    }

    /**
     * Send a response to the {@link MessageChannel} this event occured in. <br>
     * This is useful when using {@link Consumer}s as you're unable to return from within
     * an anonymous function.
     *
     * @param message The item to send to the {@link Sender} to process.
     * @param <T> Type of object to send, this type is compared to the types in our
     * {@link Sender} so we know how to send it.
     */

    public <T> void reply(T message) {
        commandler.getDispatcher().getSender().sendAsMessage(this, message, null);
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

    public Message getMessage() {
        return message;
    }

    public Commandler getCommandler() {
        return commandler;
    }

    public Confiler getConfiler() {
        return confiler;
    }

    /**
     * @return Get a list of all parameters.
     */

    public List<List<String>> getParams() {
        return params;
    }

    public AbstractMetaCommand getMetaCommand() {
        return metaCommand;
    }
}
