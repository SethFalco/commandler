/*
 * Copyright 2019-2019 Elypia CIC
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

package org.elypia.commandler.event;

import org.elypia.commandler.*;
import org.elypia.commandler.api.*;
import org.elypia.commandler.metadata.*;

/**
 * The Abstract Event object for Commandler, this can be extended for specialised
 * {@link Dispatcher}s to allow more fields.
 * This is what {@link Commandler} will fire whenever a user performs an action.
 *
 * @param <S> The source event that this is wrapping around.
 * @param <M> The type of message supported by this {@link Integration},
 *            this is specified for stricter typing and to reduce the need to cast.
 * @author seth@elypia.org (Seth Falco)
 */
public class ActionEvent<S, M> {

    /** The request provided by the {@link Integration}. */
    private final Request<S, M> request;

    /** Represents the action performed by user, like as module/command and params. */
    private final Action action;

    /** The data associated with the selected module. */
    private MetaController metaController;

    /** The data associated with the selected command. */
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
