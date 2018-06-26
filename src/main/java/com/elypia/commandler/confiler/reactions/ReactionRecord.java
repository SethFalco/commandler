package com.elypia.commandler.confiler.reactions;

import com.elypia.commandler.annotations.Command;
import com.elypia.commandler.modules.CommandHandler;
import com.elypia.commandler.parsing.Parser;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.util.*;

public class ReactionRecord {

    /**
     * The command ID as specified on the {@link Command @Command}
     * annotations in a {@link CommandHandler}.
     */

    private int commandId;

    /**
     * The {@link MessageChannel} ID of the channel the
     */

    private long channelId;
    private long messageId;
    private long ownerId;

    /**
     * The parameters the user specified as in raw text, these are
     * the raw parameters as we recieved them on Discord.
     */

    private Map<String, List<String>> params;

    /**
     * The parameters after being parsed through {@link Parser}.
     * This would be the actual parameters used to invoke the command.
     */

    private Object[] parsedParams;

    private Map<String, Object> objectStore;

    public int getCommandId() {
        return commandId;
    }

    public void setCommandId(int commandId) {
        this.commandId = commandId;
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

    public List<String> getParam(String name) {
        return params.get(name);
    }

    public void setParams(Map<String, List<String>> params) {
        this.params = params;
    }

    public Object[] getParsedParams() {
        return parsedParams;
    }

    public void setParsedParams(Object[] parsedParams) {
        this.parsedParams = parsedParams;
    }

    public Map<String, Object> getObjectStore() {
        return objectStore;
    }

    public void setObjectStore(Map<String, Object> objectStore) {
        this.objectStore = objectStore;
    }
}
