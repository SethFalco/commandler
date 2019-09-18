package org.elypia.commandler.event;

import org.elypia.commandler.api.Integration;
import org.elypia.commandler.metadata.*;

import java.io.Serializable;
import java.util.*;

/**
 * Stores user interaction with Commandler.
 * Action is {@link Serializable} to that it can be
 * stored away and retuned again by future events.
 *
 * An instance is created for every action performed by the user
 * that was picked up by a registered Dispatcher.
 *
 * Action should only contain serializable information that is relevent
 * for all command types.
 */
public class Action implements Serializable {

    public static final long serialVersionUID = 400L;

    /** ID of this action provided by {@link Integration#getActionId(Object)}. */
    private final Serializable id;

    /** The actual content of the message or event. */
    private String content;

    /** The data associated with the selected module. */
    private String controllerName;

    /** The data associated with the selected command. */
    private  String controlName;

    /** All parameters the user specified. This list is <strong>never</strong> null. */
    private List<List<String>> params;

    /**
     * Calls {@link #Action(Serializable, String, String, String)}
     * with a default id.
     *
     * This should never be used unless the {@link Integration} has no means
     * of referencing previous {@link Action actions}.
     *
     * @param content The content the user has sent.
     * @param controllerName The name of the {@link MetaController}.
     * @param controlName The name of the {@link MetaCommand}.
     */
    public Action(String content, String controllerName, String controlName) {
        this(0, content, controllerName, controlName);
    }

    /**
     * Calls {@link #Action(Serializable, String, String, String, List)} but
     * defaults the parameters `params` to an empty {@link ArrayList}.
     *
     * @param id The ID of this action, this should be referenable by other commands.
     * @param content The content the user has sent.
     * @param controllerName The name of the {@link MetaController}.
     * @param controlName The name of the {@link MetaCommand}.
     */
    public Action(Serializable id, String content, String controllerName, String controlName) {
        this(id, content, controllerName, controlName, List.of());
    }

    /**
     * Create an instance of an Action.
     *
     * @param id The ID of this action, this should be referenable by other commands.
     * @param content The content the user has sent.
     * @param controllerName The name of the {@link MetaController}.
     * @param controlName The name of the {@link MetaCommand}.
     * @param params A list of parameters the user provided.
     */
    public Action(Serializable id, String content, String controllerName, String controlName, List<List<String>> params) {
        this.id = id;
        this.content = content;
        this.controllerName = controllerName;
        this.controlName = controlName;
        this.params = params;
    }

    /**
     * @return The parameters in a user displayable state.
     */
    public String toParamString() {
        if (params.isEmpty())
            return "None";

        final StringJoiner joiner = new StringJoiner(" ");
        int i = 1;

        for (List<String> items : params) {
            joiner.add("(" + i++ + ")");

            if (items.size() == 1)
                joiner.add(items.get(0));
            else
                joiner.add("[" + String.join(", ", items) + "]");
        }

        return joiner.toString();
    }


    public Serializable getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getControllerName() {
        return controllerName;
    }

    public String getControlName() {
        return controlName;
    }

    public List<List<String>> getParams() {
        return params;
    }

    @Override
    public String toString() {
        return controllerName + " > " + controlName + " | " + toParamString();
    }
}
