/*
 * Copyright 2019-2025 Seth Falco and Commandler Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fun.falco.commandler.event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import fun.falco.commandler.api.Integration;
import fun.falco.commandler.metadata.MetaCommand;
import fun.falco.commandler.metadata.MetaController;

/**
 * Stores user interaction with Commandler. Action is {@link Serializable} to
 * that it can be stored away and returned again by future events.
 *
 * <p>Created for every action performed by the user  that was picked up by a
 * registered Dispatcher.</p>
 *
 * <p>Action should only contain serializable information that is relevant for all
 * command types.</p>
 *
 * @author seth@falco.fun (Seth Falco)
 */
public class Action implements Serializable {

    public static final long serialVersionUID = 400L;

    /** ID of this action provided by {@link Integration#getActionId(Object)}. */
    protected final Serializable id;

    /** The actual content of the message or event. */
    protected String content;

    /** The name associated with the selected module. */
    protected Class<?> controllerType;

    /** The name associated with the selected command. */
    protected String methodName;

    /** All parameters the user specified. This list is <strong>never</strong> null. */
    protected transient List<List<String>> params;

    /**
     * Calls {@link #Action(Serializable, String, Class, String)} with a default
     * id.
     *
     * <p>This should never be used unless the {@link Integration} has no means of
     * referencing previous {@link Action actions}.</p>
     *
     * @param content Content the user has sent.
     * @param controllerType Name of the {@link MetaController}.
     * @param methodName Name of the {@link MetaCommand}.
     */
    public Action(String content, Class<?> controllerType, String methodName) {
        this(0, content, controllerType, methodName);
    }

    /**
     * Calls {@link #Action(Serializable, String, Class, String, List)} but
     * defaults the parameters `params` to an empty {@link ArrayList}.
     *
     * @param id
     *      ID of this action, this should be referenceable by other commands.
     * @param content Content the user has sent.
     * @param controllerType Name of the {@link MetaController}.
     * @param methodName Name of the {@link MetaCommand}.
     */
    public Action(Serializable id, String content, Class<?> controllerType, String methodName) {
        this(id, content, controllerType, methodName, List.of());
    }

    /**
     * Create an instance of an Action.
     *
     * @param id
     *     ID of this action, this should be referenceable by other commands.
     * @param content Content the user has sent.
     * @param controllerType Name of the {@link MetaController}.
     * @param methodName Name of the {@link MetaCommand}.
     * @param params List of parameters the user provided.
     */
    public Action(Serializable id, String content, Class<?> controllerType, String methodName, List<List<String>> params) {
        this.id = id;
        this.content = content;
        this.controllerType = controllerType;
        this.methodName = methodName;
        this.params = params;
    }

    /**
     * @return Parameters in a user displayable state.
     */
    public String toParamString() {
        if (params.isEmpty()) {
            return "None";
        }

        final StringJoiner joiner = new StringJoiner(" ");
        int i = 1;

        for (List<String> items : params) {
            joiner.add("(" + i++ + ")");

            if (items.size() == 1) {
                joiner.add(items.get(0));
            } else {
                joiner.add("[" + String.join(", ", items) + "]");
            }
        }

        return joiner.toString();
    }


    public Serializable getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public Class<?> getControllerType() {
        return controllerType;
    }

    public String getMethodName() {
        return methodName;
    }

    public List<List<String>> getParams() {
        return params;
    }

    @Override
    public String toString() {
        return controllerType + " > " + methodName + " | " + toParamString();
    }
}
