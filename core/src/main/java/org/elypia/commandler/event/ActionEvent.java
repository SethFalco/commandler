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

import org.elypia.commandler.Commandler;
import org.elypia.commandler.api.Integration;
import org.elypia.commandler.metadata.*;

import java.util.Locale;

/**
 * The Abstract Event object for Commandler, this can be extended for specialised
 * {@link org.elypia.commandler.api.Dispatcher}s to allow more fields.
 * This is what {@link Commandler} will fire whenever a user performs an action.
 *
 * @param <S> The source event that this is wrapping around.
 * @param <M> The type of message supported by this {@link Integration},
 *            this is specified for stricter typing and to reduce the need to cast.
 * @author seth@elypia.org (Syed Shah)
 */
public class ActionEvent<S, M> {

    /** The controller this event came from. */
    private final Integration<S, M> integration;

    /** The event that was fired from the controller. */
    private final S source;

    /** Represents the action performed by user, like as module/command and params. */
    private final Action action;

    /** The data associated with the selected module. */
    private MetaController metaController;

    /** The data associated with the selected command. */
    private MetaCommand metaCommand;

    /** The locale associated with this event. */
    private Locale locale;

    public ActionEvent(Integration<S, M> integration, S source, Action action) {
        this.integration = integration;
        this.source = source;
        this.action = action;
    }

    public Integration<S, M> getIntegration() {
        return integration;
    }

    public S getSource() {
        return source;
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

    public Locale getLocale() {
        return locale;
    }
}
