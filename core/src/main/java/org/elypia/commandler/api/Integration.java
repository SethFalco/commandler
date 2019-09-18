package org.elypia.commandler.api;

import java.io.Serializable;

/**
 * An integration represents an class which manages the integration
 * or implementation of Commandler with another service or platform.
 *
 * This could represent a terminal if it's a console application
 * or a form of social media such as Discord.
 *
 * @param <S> The type of source event this integration consumes. If there
 * are multiple types, specify the highest level expected to be used.
 * @param <M> The type of message this platform sends and receives.
 */
public interface Integration<S, M> {

    /**
     * @return The type of object that is sent and received by this integration.
     */
    Class<M> getMessageType();

    /**
     * @param source The event this integration has recieved.
     * @return A unique and {@link Serializable} ID that represents this action.
     */
    Serializable getActionId(S source);
}
