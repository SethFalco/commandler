package com.elypia.commandler.impl;

import com.elypia.commandler.*;
import com.elypia.commandler.interfaces.ICommandEvent;

import java.util.Map;

/**
 * The {@link CommandEvent} is the event object produced and used
 * by {@link Commandler} using the the {@link E event} spawned from the
 * {@link C client}. This should contain convnience methods for the event
 * as well as manage any platform specific implementations.
 *
 * @param <C> The client we're integrating with.
 * @param <E> The event that the client spawns.
 * @param <M> The message type that users send and receieve.
 */
public class CommandEvent<C, E, M> implements ICommandEvent<C, E, M> {

    private boolean invalidated;

    private M error;

    private CommandInput input;

    private C client;

    private E source;

    private Commandler<C, E, M> commandler;

    private LanguageEngine<E> engine;

    public CommandEvent(Commandler<C, E, M> commandler, CommandInput input) {
        this.commandler = commandler;
        this.input = input;

        engine = commandler.getEngine();
    }

    /**
     * Sends a response in the channel this event occured in. It's favourable to
     * use this over the native reply or send message method provides
     * by the client because this allows us to utilise our builders
     * and implementation around it.
     *
     * @param output The item to send to the {@link MessageBuilder} to process.
     * @return The message that was built by the {@link MessageBuilder}.
     */
    @Override
    public <T> M send(T output) {
        return getCommandler().getBuilder().build(this, output);
    }

    /**
     * Send a reply using {@link LanguageEngine#getScript(Object, String, Object...)}
     * for obtaining a script through your implementation.
     *
     * @param key The key associated with the script to obtain.
     * @return A script associated with this key.
     */
    public M sendScript(String key) {
        return sendScript(key, Map.of());
    }

    public <T> M sendScript(String key, Map<String, T> params) {
        return send(engine.getScript(getSource(), key, params));
    }

    public M trigger(String trigger) {
        return trigger(trigger, true);
    }

    /**
     * Trigger another commands which branches from this commands. The new commands
     * will use the same {@link E event} and {@link M message} but with the command
     * specified instead. <br>
     *
     * @param trigger The new commands to process instead.
     */
    public M trigger(String trigger, boolean send) {
        return getCommandler().execute(getSource(), trigger, send);
    }

    /**
     * This indicates that the event has been invalidated. If the error
     * should be reported to the user then this should set {@link #error}
     * to a non-null value (the object to be sent to the user).
     *
     * @param reason Why the message was invalidated if the user should know.
     */
    @Override
    public void invalidate(Object reason) {
        invalidated = true;

        if (reason != null)
            error = send(reason);
    }

    public Commandler<C, E, M> getCommandler() {
        return commandler;
    }

    public CommandInput getInput() {
        return input;
    }

    public M getError() {
        return error;
    }

    public C getClient() {
        return client;
    }

    public E getSource() {
        return source;
    }
}
