package com.elypia.commandler.interfaces;

import com.elypia.commandler.*;

import java.util.Map;

/**
 * The {@link ICommandEvent} is the event object produced and used
 * by {@link Commandler} using the the {@link S event} spawned from the
 * client. This should contain convnience methods for the event
 * as well as manage any platform specific implementations.
 *
 * @param <S> The event that the client spawns.
 * @param <M> The message type that users send and receieve.
 */
public interface ICommandEvent<S, M> {

    /**
     * Sends a response in the channel this event occured in. It's favourable to
     * use this over the native reply or send message method provides
     * by the client because this allows us to utilise our builders
     * and implementation around it.
     *
     * @param output The item to send to the {@link ResponseBuilder} to process.
     * @return The message that was built by the {@link ResponseBuilder}.
     */
    <T> M send(T output);

    /**
     * Send a reply using {@link IScripts}
     * for obtaining a script through your implementation.
     *
     * @param key The key associated with the script to obtain.
     * @return A script associated with this key.
     */
    default M send(String key) {
        return send(key, Map.of());
    }

    <T> M send(String key, Map<String, T> params);

    default M trigger(String trigger) {
        return trigger(trigger, true);
    }

    /**
     * Trigger another commands which branches from this commands. The new commands
     * will use the same {@link S event} and {@link M message} but with the command
     * specified instead. <br>
     *
     * @param trigger The new commands to process instead.
     */
    default M trigger(String trigger, boolean send) {
        return getCommandler().execute(getSource(), trigger, send);
    }

    boolean isValid();

    /**
     * Invalidate the event, if a command follows a valid format, however
     * is deemed invalid during processing of the event, it should be
     * invalidated which will send the relevent error message if non-null
     * and prevent further processing.
     *
     * @param reason The reason to send in chat, or null if we should fail silently.
     */
    void invalidate(String reason);

    Commandler<S, M> getCommandler();

    CommandInput getInput();

    S getSource();

    String getError();
}
