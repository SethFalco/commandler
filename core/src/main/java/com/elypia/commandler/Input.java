package com.elypia.commandler;

import com.elypia.commandler.metadata.data.*;

import java.util.*;

/**
 * The input object created when parsing user input.
 * If the user performed a valid command all fields should be
 * populated.
 */
public class Input {

    /** Format used for {@link #toParamString()}. */
    private static final String PARAM_FORMAT = "(%,d) %s";

    /** The actual content of the message. */
    private String content;

    /** All parameters the user specified. This list is <strong>never</strong> null. */
    private List<List<String>> params;

    /** The data associated with the selected module. */
    private MetaModule module;

    /** The data associated with the selected command. */
    private MetaCommand command;

    /**
     * The raw user input as is. None of the parameters can be null.
     *
     * @param content The original message content.
     * @param module The module containing the command initiated.
     * @param command The command initiated.
     * @param params The parameters to pass to the command.
     */
    public Input(String content, MetaModule module, MetaCommand command, List<List<String>> params) {
        this.content = Objects.requireNonNull(content);
        this.module = Objects.requireNonNull(module);
        this.command = Objects.requireNonNull(command);
        this.params = Objects.requireNonNull(params);
    }

    /**
     * @return The parameters in a user displayable state.
     */
    public String toParamString() {
        final StringJoiner paramJoiner = new StringJoiner(", ");

        for (List<String> items : params) {
            if (items.size() == 1)
                paramJoiner.add("'" + items.get(0) + "'");

            else {
                StringJoiner itemJoiner = new StringJoiner(", ");

                for (String item : items)
                    itemJoiner.add("'" + item + "'");

                paramJoiner.add("[" + itemJoiner.toString() + "]");
            }
        }

        return String.format(PARAM_FORMAT, params.size(), paramJoiner.toString());
    }

    public String getContent() {
        return content;
    }

    public MetaModule getModule() {
        return module;
    }

    public MetaCommand getCommand() {
        return command;
    }

    public List<List<String>> getParams() {
        return params;
    }

    /**
     * @return The total number of parameters.
     */
    public int getParamCount() {
        return params.size();
    }

    @Override
    public String toString() {
        return module.getName() + " > " + command.getName() + " | (" + command.getParams().size() + ")";
    }
}
