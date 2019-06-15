package com.elypia.commandler;

import com.elypia.commandler.metadata.data.*;

import java.util.*;

/**
 * The input object created when parsing user input.
 * If the user performed a valid command all fields should be
 * populated.
 */
public class Input {

    /** The actual content of the message. */
    private String content;

    /** The prefix the user used. */
    private String prefix;

    /** All parameters the user specified. This list is <strong>never</strong> null. */
    private List<List<String>> parameters;

    /** The data associated with the selected module. */
    private MetaModule module;

    /** The data associated with the selected command. */
    private MetaCommand command;

    /**
     * The raw user input as is. None of the parameters can be null.
     *
     * @param content The original message content.
     * @param prefix The prefix used to initiate this command.
     * @param module The module containing the command initiated.
     * @param command The command initiated.
     * @param parameters The parameters to pass to the command.
     */
    public Input(String content, String prefix, MetaModule module, MetaCommand command, List<List<String>> parameters) {
        this.content = Objects.requireNonNull(content);
        this.prefix = Objects.requireNonNull(prefix);
        this.content = Objects.requireNonNull(prefix);
        this.module = Objects.requireNonNull(module);
        this.command = Objects.requireNonNull(command);
        this.parameters = Objects.requireNonNull(parameters);
    }

    public String getContent() {
        return content;
    }

    public String getPrefix() {
        return prefix;
    }

    public MetaModule getModule() {
        return module;
    }

    public MetaCommand getCommand() {
        return command;
    }

    public List<List<String>> getParameters() {
        return parameters;
    }

    /**
     * @return The total number of parameters.
     */
    public int getParameterCount() {
        return parameters.size();
    }

    @Override
    public String toString() {
        return module.getName() + " > " + command.getName() + " | (" + command.getParams().size() + ")";
    }
}
