package com.elypia.commandler.event;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.api.Integration;
import com.elypia.commandler.metadata.*;

import java.util.Locale;

/**
 * The Abstract Event object for Commandler, this can be extended for specialised
 * {@link com.elypia.commandler.api.Dispatcher}s to allow more fields.
 * This is what {@link Commandler} will fire whenever a user performs an action.
 *
 * @param <S> The source event that this is wrapping around.
 * @param <M> The type of message supported by this {@link Integration},
 *            this is specified for stricter typing and to reduce the need to cast.
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
