package com.elypia.commandler.jda.reactions;

import com.elypia.commandler.CommandInput;
import com.elypia.commandler.annotations.Command;
import com.elypia.commandler.impl.IHandler;
import net.dv8tion.jda.core.entities.*;

import java.util.Map;

/**
 * A ReactionRecord is a record of some basic information
 * associated with a reaction events which should persist
 * between instances of the bot. <br>
 * The default implementation, {@link DefaultReactionController}
 * stores the events in a {@link Map}, while this works,
 * should the bot restart all reactions will no longer respond immediatly. <br>
 * When putting data in ReactionRecords it's better not stick to storing
 * primitives to avoid hassle when trying to persist data by storing
 * records in a database or file.
 */
public class ReactionRecord {

    /**
     * The command ID as specified on the {@link Command @Command}
     * annotations in a {@link IHandler}.
     */
    private int commandId;

    /**
     * If the message came from a {@link TextChannel} or
     * {@link MessageChannel}.
     */
    private boolean isGuild;

    /**
     * The {@link MessageChannel} ID of the channel the channel.
     */
    private long channelId;

    /**
     * The {@link Message} ID in the {@link #channelId channel}.
     */
    private long messageId;

    /**
     * The author of the {@link #messageId message}.
     */
    private long ownerId;

    /**
     * The ID of the message that spawned this reaction record. <br>
     * This will be the message by a user that performed the command
     * to make the bot respond with a message compatible with this
     * reaction event. <br>
     * <strong>Note:</strong> When using the parent message in handling,
     * remember to check if the message has been deleted as if so getting
     * it will not be possible.
     */
    private long parentMessage;

    /**
     * The input the user provided to perform the command
     * that spawned this event.
     */
    private CommandInput input;

    /**
     * Cached data along with the command in preperation for handling records.
     * For example caching a list of items from an API call to save you
     * from making many requests everytime the reaction event is triggered.
     */ // ! May need to reconsider how we store this.
    private Map<String, Object> objectStore;

    public int getCommandId() {
        return commandId;
    }

    public void setCommandId(int commandId) {
        this.commandId = commandId;
    }

    public boolean isGuild() {
        return isGuild;
    }

    public void setGuild(boolean guild) {
        isGuild = guild;
    }

    public long getChannelId() {
        return channelId;
    }

    public void setChannelId(long channelId) {
        this.channelId = channelId;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    public long getParentMessage() {
        return parentMessage;
    }

    public void setParentMessage(long parentMessage) {
        this.parentMessage = parentMessage;
    }

    public CommandInput getInput() {
        return input;
    }

    public void setInput(CommandInput input) {
        this.input = input;
    }

    public Map<String, Object> getObjectStore() {
        return objectStore;
    }

    public void setObjectStore(Map<String, Object> objectStore) {
        this.objectStore = objectStore;
    }
}
