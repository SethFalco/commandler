package org.elypia.commandler.api;

import org.elypia.commandler.*;

/**
 * Whenever Commandler recieves an Action from any of the registered
 * {@link Integration}s, it goes through all registred {@link ActionListener}.
 *
 * One {@link ActionListener} implementation is provided by default which
 * is the {@link ActionHandler}.
 */
public interface ActionListener {

    /**
     * @param integration The integration that handed {@link Commandler} this event.
     * @param source The original event that the {@link Integration} received.
     * @param content The raw content of the message as percieved by the integration.
     * @param <M> The type of message this {@link Integration} sends and received.
     * @return The response to this action, or null if no response was given.
     */
    public <S, M> M onAction(Integration<S, M> integration, S source, String content);
}
