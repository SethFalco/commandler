package com.elypia.commandler.impl;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.components.Builders;

/**
 * The {@link ICommandEvent} is the event object produced and used
 * by {@link Commandler} using the the {@link E event} spawned from the
 * {@link C client}. This should contain convnience methods for the event
 * as well as manage any platform specific implementations.
 *
 * @param <C> The client we're integrating with.
 * @param <E> The event that the client spawns.
 * @param <M> The message type that users send and receieve.
 */
public interface ICommandEvent<C, E, M> {

    /**
     * Sends a response in the channel this event occured in. It's favourable to
     * use this over the native reply or send message method provides
     * by the client because this allows us to utilise our builders
     * and implementation around it.
     *
     * @param object The item to send to the {@link Builders} to process.
     * @return The message that was built by the {@link Builders}.
     */
    M reply(Object object);

    /**
     * Try delete the message that was sent in chat, if this isn't possible
     * for whatever reason, then do nothing.
     *
     * @return If a request was made to delete the message.
     */
    boolean deleteMessage();

    /**
     * Trigger another commands which branches from this commands. The new commands
     * will use the same {@link E event} and {@link M message} but with the command
     * specified instead. <br>
     *
     * @param trigger The new commands to process instead.
     */
    default M trigger(String trigger) {
        return getCommandler().trigger(getSourceEvent(), trigger);
    }

    void invalidate(Object reason);

    Commandler<C, E, M> getCommandler();

    E getSourceEvent();
}
