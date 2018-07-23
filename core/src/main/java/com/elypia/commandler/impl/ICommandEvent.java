package com.elypia.commandler.impl;

import com.elypia.commandler.*;
import com.elypia.commandler.registers.BuildRegister;

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
     * @param output The item to send to the {@link BuildRegister} to process.
     * @return The message that was built by the {@link BuildRegister}.
     */
    default M reply(Object output) {
        return getCommandler().getBuilder().build(this, output);
    }

    /**
     * Trigger another commands which branches from this commands. The new commands
     * will use the same {@link E event} and {@link M message} but with the command
     * specified instead. <br>
     *
     * @param trigger The new commands to process instead.
     */
    default M trigger(String trigger) {
        return getCommandler().trigger(getSource(), trigger);
    }

    void invalidate(Object reason);

    Commandler<C, E, M> getCommandler();

    CommandInput<C, E, M> getInput();

    C getClient();

    E getSource();

    M getMessage();
}
