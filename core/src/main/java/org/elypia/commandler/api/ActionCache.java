package org.elypia.commandler.api;

import org.elypia.commandler.event.Action;
import org.elypia.commandler.metadata.MetaController;

import java.io.Serializable;
import java.util.*;

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
 */
public interface ActionCache extends Iterable<Action> {

    /**
     * @param action The action to store.
     */
    void put(Action action);

    /**
     * @param serializable The id of the action.
     * @return The action that relates to this ID, or null if
     * no such action was cached. (This doesn't mean the ID was invalid
     * or wasn't never an action that was handled, just that it wasn't stored.)
     */
    Action get(Serializable serializable);

    /**
     * @param serializable The id of the action to get and delete.
     * The action that relates to this ID, or null if
     * no such action was cached. (This doesn't mean the ID was invalid
     * or wasn't never an action that was handled, just that it wasn't stored.)
     */
    Action pop(Serializable serializable);

    /**
     * @param serializable The id of the action to delete.
     */
    void remove(Serializable serializable);

    /**
     * @return A list of all cached actions in this instance.
     */
    List<Action> getAll();

    @Override
    default Iterator<Action> iterator() {
        return getAll().iterator();
    }
}
