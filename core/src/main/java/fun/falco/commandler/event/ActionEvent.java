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

import fun.falco.commandler.Commandler;
import fun.falco.commandler.api.Dispatcher;
import fun.falco.commandler.api.Integration;
import fun.falco.commandler.metadata.MetaCommand;
import fun.falco.commandler.metadata.MetaController;

/**
 * Abstract Event object for Commandler, this can be extended for specialized
 * {@link Dispatcher}s to allow more fields. This is what {@link Commandler}
 * will fire whenever a user performs an action.
 *
 * @author seth@falco.fun (Seth Falco)
 * @param <S> Source event that this is wrapping around.
 * @param <M>
 *     Type of message supported by this {@link Integration}, this is specified
 *     for stricter typing and to reduce the need to cast.
 */
public class ActionEvent<S, M> {

    /** Request provided by the {@link Integration}. */
    private final Request<S, M> request;

    /** Represents the action performed by user, like as module/command and params. */
    private final Action action;

    /** Data associated with the selected module. */
    private MetaController metaController;

    /** Data associated with the selected command. */
    private MetaCommand metaCommand;

    public ActionEvent(Request<S, M> request, Action action, MetaController metaController, MetaCommand metaCommand) {
        this.request = request;
        this.action = action;
        this.metaController = metaController;
        this.metaCommand = metaCommand;
    }

    public Request<S, M> getRequest() {
        return request;
    }

    public Action getAction() {
        return action;
    }

    public MetaController getMetaController() {
        return metaController;
    }

    public MetaCommand getMetaCommand() {
        return metaCommand;
    }
}
