package com.elypia.commandler.impl;

import com.elypia.commandler.*;

import java.util.Map;

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
    default <T> M reply(T output) {
        return getCommandler().getBuilder().build(this, output);
    }

    /**
     * Send a reply using {@link Confiler#sendScript(Object, String)}
     * for obtaining a script through your implementation.
     *
     * @param key The key associated with the script to obtain.
     * @return A script associated with this key.
     */
    default M sendScript(String key) {
        return sendScript(key, Map.of());
    }

    default <T> M sendScript(String key, Map<String, T> params) {
        return reply(getScript(key, params));
    }

    default String getScript(String key) {
        return getScript(key, Map.of());
    }

    default <T> String getScript(String key, Map<String, T> params) {
        return getCommandler().getConfiler().getScript(getSource(), key, params);
    }

    default M trigger(String trigger) {
        return trigger(trigger, true);
    }

    /**
     * Trigger another commands which branches from this commands. The new commands
     * will use the same {@link E event} and {@link M message} but with the command
     * specified instead. <br>
     *
     * @param trigger The new commands to process instead.
     */
    default M trigger(String trigger, boolean send) {
        return getCommandler().trigger(getSource(), trigger, send);
    }

    /**
     * Invalidate the event, if a command follows a valid format, however
     * is deemed invalid during processing of the event, it should be
     * invalidated which will send the relevent error message if non-null
     * and prevent further processing.
     *
     * @param reason The reason to send in chat, or null if we should fail silently.
     */
    void invalidate(Object reason);

    Commandler<C, E, M> getCommandler();

    CommandInput<C, E, M> getInput();

    C getClient();

    E getSource();
}
