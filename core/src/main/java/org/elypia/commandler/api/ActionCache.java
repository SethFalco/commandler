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

package org.elypia.commandler.api;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

import org.elypia.commandler.event.Action;
import org.elypia.commandler.metadata.MetaController;

/**
 * The {@link ActionCache} allows caching a {@link Serializable}
 * identifier of an action or message to be cached against metadata regarding
 * the {@link Integration} and {@link MetaController} associated with it.
 *
 * This should only cache events that have been interacted with are
 * of interest of the Commandler application.
 *
 * This is helpful for linking commands together, or allowing additional commands
 * on top of an existing message that has already been sent.
 *
 * It's strongly recommended to implement this yourself with something
 * more persistent such as a database.
 *
 * @author seth@falco.fun (Seth Falco)
 */
public interface ActionCache extends Iterable<Action> {

    /**
     * @param action Action to store.
     */
    void put(Action action);

    /**
     * @param serializable ID of the action.
     * @return
     *     Action that relates to this ID, or null if no such action was cached.
     *     (This doesn't mean the ID was invalid or wasn't never an action that
     *     was handled, just that it wasn't stored.)
     */
    Action get(Serializable serializable);

    /**
     * @param serializable
     *     ID of the action to get and delete. The action that relates to this
     *     ID, or null if no such action was cached. (This doesn't mean the ID
     *     was invalid or wasn't ever an action that was handled, just that it
     *     wasn't stored.)
     * @return Action that was previous represented by this key.
     */
    Action remove(Serializable serializable);

    /**
     * @return List of all cached actions in this instance.
     */
    Collection<Action> getAll();

    @Override
    default Iterator<Action> iterator() {
        return getAll().iterator();
    }
}
